package scraper

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener
import common.throwsServiceException
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import repository.ItemData


abstract class WebScrapper(var root: String) {
    fun findData(type: String): Map<String, List<ItemData>> {
        val doc = getHtmlDocument(root)
        return findInfo(doc, type)
    }

    fun getHtmlDocument(url: String): Document = throwsServiceException {
        var client = WebClient(BrowserVersion.BEST_SUPPORTED)
        println("=====================================================================================================")
        println("Going to page $url")
        println("=====================================================================================================")
        val page: HtmlPage = client.getPage(url)
        client.options.isCssEnabled = false
        client.options.isDownloadImages = false
        client.cssErrorHandler = SilentCssErrorHandler()
        client.javaScriptErrorListener = SilentJavaScriptErrorListener()
        client.getOptions().setJavaScriptEnabled(true)
        client.waitForBackgroundJavaScript(5000)
        return@throwsServiceException Jsoup.parse(page.asXml());
    }

    protected abstract fun findInfo(doc: Document, type: String = ""): Map<String, List<ItemData>>
}

class LDLCOportunitiesScrapper(root: String = "https://www.ldlc.com/es-es/n2193/oportunidades/", val retard: Int = 4) : WebScrapper(root) {

    val response = mutableMapOf<String, MutableList<ItemData>>()

    override fun findInfo(doc: Document, type: String) = runBlocking {
        doc.findCategories().map {
            if (type.equals("any") || it.title().equals(type, ignoreCase = true)) {
                println("wait $retard secs")
                delay(retard * 1000L)
                println("GO!")
                findProducts(it, type)
            } else {
                println("Skipping ${it.title()} we are searching for $type")
            }
        }
        return@runBlocking response
    }

    private fun findProducts(it: Element, type: String) {
        val page = getHtmlDocument(getAbsoluteURL(it.href()))
        page.products().map { putElement(page.category(), buildItemData(it)) }
        followPagination(page, type)
    }


    private fun followPagination(page: Document, type: String) {
        val next = page.next()
        if (next.size > 0) {
            findProducts(next.first(), type)
        }
    }

    private fun Element.href() = this.attributes().get("href")
    private fun Element.title() = this.child(0)?.child(0)?.text() ?: "NO"
    private fun Document.category() = this.select(".lastBreadcrumb").text()
    private fun Document.products() = this.select(".listing-product ul li")!!
    private fun Document.next() = this.select(".pagination .next a")
    private fun Document.findCategories() = this.select(".categories a")

    private fun getAbsoluteURL(url: String): String {
        if (url.startsWith("/")) {
            return "https://www.ldlc.com$url"
        }
        return url
    }

    private fun putElement(key: String, item : ItemData) {
        if (!response.containsKey(key)) {
            response.put(key, mutableListOf())
        }
        response.get(key)!!.add(item)
    }

    private fun buildItemData(it: Element): ItemData {
        var metadata = mapOf<String, Any>(
            "previous" to "N/A",
            "pic" to it.select(".pic a img").attr("src")
        )
        val regex = "[^\\d]".toRegex()
        return ItemData(
            it.select(".title-3")?.text() ?: "N/A",
            it.select(".desc")?.text() ?: "N/A",
                "0${regex.replace(it.select(".price .price").text(), "")}"
                    .toInt(),
            metadata
        )
    }

}

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async(Dispatchers.IO) { f(it) } }.map { it.await() }
}


