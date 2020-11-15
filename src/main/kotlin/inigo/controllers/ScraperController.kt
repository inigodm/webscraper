package inigo.controllers

import com.google.gson.Gson
import inigo.exceptions.IncorrectNumberOfParams
import inigo.repository.RepositoryConnection
import inigo.repository.RepositoryManager
import inigo.worker.InfoRetriever
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "SCRAPER", value = ["/scrap/*"])
class ScraperController(
    var logger:Logger = LoggerFactory.getLogger(ScraperController::class.java)) : HttpServlet() {
    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        logger.info("GET in scraper")
        val repo = RepositoryManager(RepositoryConnection("scraper.db"))
        val params = getParamsAsMap(req.getRequestURI(), "/web/")
        res.writer.write(Gson().toJson(repo.findProductsOf(params.get("scrap")!!,
            params.get("type") ?: "",
            params.get("query") ?: "")))
        logger.info("END GET in scraper")
        System.gc()
    }

    fun getParamsAsMap(uri: String, urlBase: String): Map<String, String> {
        return uri.replaceFirst("$urlBase", "").split("/").toMap()
    }

    override fun doPut(req: HttpServletRequest, res: HttpServletResponse) {
        logger.info("PUT in scraper")
        val repo = RepositoryManager(RepositoryConnection("scraper.db"))
        val params = getParamsAsMap(req.getRequestURI(), "/web/")
        val infoRetriever = InfoRetriever(repo)
        infoRetriever.updateProductDataForPage(params.get("type") ?: "")
        res.writer.write(Gson().toJson(repo.findProductsOf(params.get("scrap")!!)))
        logger.info("END PUT in scraper")
        System.gc()
    }

    override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    }

    override fun doDelete(req: HttpServletRequest, res: HttpServletResponse) {
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
    }
}

@WebServlet(name = "ALERT", value = ["/alert/*"])
class AlertController(
    var logger:Logger = LoggerFactory.getLogger(AlertController::class.java)) : HttpServlet() {
    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        logger.info("GET in alert")
        val repo = RepositoryManager(RepositoryConnection("scraper.db"))
        val params = getParamsAsMap(req.getRequestURI(), "/web/")
        res.writer.write(Gson().toJson(repo.findNewProductsIn(params.get("alert")!!, params.get("type")?: "")))
        logger.info("END GET in alert")
        System.gc()
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
