package inigo.scraper

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import inigo.common.throwsServiceException
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import inigo.repository.ItemData
import inigo.repository.RepositoryManager

abstract class WebScrapper(var root: String, var logger: Logger = LoggerFactory.getLogger(WebScrapper::class.java)) {
    protected fun getHtmlDocument(url: String): Document = throwsServiceException (url){
        val client = WebClient(BrowserVersion.BEST_SUPPORTED)
        client.options.isCssEnabled = false
        client.options.isDownloadImages = false
        client.cssErrorHandler = SilentCssErrorHandler()
        client.javaScriptErrorListener = SilentJavaScriptErrorListener()
        client.getOptions().setJavaScriptEnabled(true)
        client.waitForBackgroundJavaScript(5000)
        logger.trace("=====================================================================================================")
        logger.trace("Going to page $url")
        logger.trace("=====================================================================================================")
        val page: HtmlPage = client.getPage(url)
        client.close()
        return@throwsServiceException Jsoup.parse(page.asXml());
    }

    abstract fun updateData(type: String)
}

class LDLCOportunitiesScrapper(var repo: RepositoryManager?,
                               root: String = "https://www.ldlc.com/es-es/n2193/oportunidades/",
                               val retard: Int = 4) : WebScrapper(root) {

    override fun updateData(type: String) {
        repo!!.forgetProductsFromPageAndType("ldlc", type)
        val categoriesUrls = getCategoriesUrls(getHtmlDocument(root), type)
        categoriesUrls.forEach{ updateCategoryData(it.second, it.first) }
        repo = null
    }

    fun getCategoriesUrls(doc: Document, type: String): List<Pair<String, String>> {
        val urls = mutableListOf<Pair<String, String>>()
        doc.findCategories().map {
            if ((type.equals("any") && !it.title().equals("NOOO")) || it.title().equals(type, ignoreCase = true)) {
                logger.trace("Added to search ${it.href()}")
                urls.add(Pair(it.title(), it.href()))
            } else {
                logger.trace("Skipping ${it.title()} we are searching for $type")
            }
        }
        return urls
    }

    fun updateCategoryData(categoryUrl: String, type: String) {
        val urls = mutableListOf(categoryUrl)
        while (urls.isNotEmpty()) {
            updatePage(urls.removeAt(0), type, urls)
        }
    }

    fun updatePage(url: String, type: String, urlsToVisit: MutableList<String>) = runBlocking{
        delay(retard * 1000L)
        val page = getHtmlDocument(getAbsoluteURL(url))
        page.products().map {
            repo!!.saveProductData(buildItemData(it, type))
        }
        val next = page.next()
        if (next.size > 0) {
            urlsToVisit.add(next.first().href())
        }
    }

    private fun getAbsoluteURL(url: String): String {
        if (url.startsWith("/")) {
            return "https://www.ldlc.com$url"
        }
        return url
    }

    private fun buildItemData(it: Element, type: String): ItemData {
        val metadata = mutableMapOf<String, Any>(
            "previous" to "N/A",
            "pic" to it.select(".pic a img").attr("src")
        )
        val regex = "[^\\d]".toRegex()
        return ItemData(
            name =it.select(".title-3")?.text() ?: "N/A",
            desc =it.select(".desc")?.text() ?: "N/A",
            price = "0${regex.replace(it.select(".price .price").text(), "")}"
                    .toInt(),
            extra = metadata,
            type = type.replace(" ", ""),
            page = "ldlc",
            url = "https://www.ldlc.com${it.select(".pic a").attr("href")}"
        )
    }
}
private fun Element.href() = this.attributes().get("href")
private fun Element.title() =
    try {
        this.child(0).attributes().get("alt")
    } catch (e: Throwable) {
        "NOOO"
    }

private fun Document.products() = this.select(".listing-product ul li")!!
private fun Document.next() = this.select(".pagination .next a")
private fun Document.findCategories() = this.select(".categories a")
