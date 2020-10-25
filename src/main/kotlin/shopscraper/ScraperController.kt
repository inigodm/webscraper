package shopscraper

import API.ScraperSelector
import com.google.gson.Gson
import exceptions.IncorrectNumberOfParams
import repository.RepositoryConnection
import repository.RepositoryManager
import worker.InfoRetriever
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "SCRAPER", value = ["/scrap/*"])
class ScraperController : HttpServlet() {
    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        val repo = RepositoryManager(RepositoryConnection("/home/tomcat7/scraper.db"))
        val params = getParamsAsMap(req.getRequestURI(), "/web/")
        res.writer.write(Gson().toJson(repo.findProductsOf(params.get("scrap")!!,
            params.get("type") ?: "",
            params.get("query") ?: "")))
    }

    fun getParamsAsMap(uri: String, urlBase: String): Map<String, String> {
        return uri.replaceFirst("$urlBase", "").split("/").toMap()
    }

    override fun doPut(req: HttpServletRequest, res: HttpServletResponse) {
        val repo = RepositoryManager(RepositoryConnection("scraper.db"))
        val params = getParamsAsMap(req.getRequestURI(), "/web/")
        val infoRetriever = InfoRetriever(repo, ScraperSelector())
        infoRetriever.updateProductDataForPage(params.get("scrap")!!, params.get("type") ?: "")
        res.writer.write(Gson().toJson(repo.findProductsOf(params.get("scrap")!!)))
    }

    override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    }

    override fun doDelete(req: HttpServletRequest, res: HttpServletResponse) {
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    }
}

@WebServlet(name = "ALERT", value = ["/alert/*"])
class AlertController : HttpServlet() {
    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        val repo = RepositoryManager(RepositoryConnection("scraper.db"))
        val params = getParamsAsMap(req.getRequestURI(), "/web/")
        res.writer.write(Gson().toJson(repo.findNewProductsIn(params.get("alert")!!, params.get("type")?: "")))
    }

    fun getParamsAsMap(uri: String, urlBase: String): Map<String, String> {
        return uri.replaceFirst("$urlBase", "").split("/").toMap()
    }

    override fun doPut(req: HttpServletRequest, res: HttpServletResponse) {
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    }

    override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    }

    override fun doDelete(req: HttpServletRequest, res: HttpServletResponse) {
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    }
}


fun <T> List<T>.toMap(): MutableMap<T, T> {
    if (this.size % 2 > 0) throw IncorrectNumberOfParams.becauseMustBePair()
    var aux: T? = null
    val res = mutableMapOf<T, T>()
    this.forEach {
        if (aux == null) {
            aux = it
        } else {
            res.put(aux!!, it)
            aux = null
        }
    }
    return res
}
