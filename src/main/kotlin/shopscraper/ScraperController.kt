package shopscraper

import API.RestClient
import API.ScraperSelector
import com.google.gson.Gson
import exceptions.IncorrectNumberOfParams
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "SCRAPER", value = ["/scrap/*"])
class ScraperController : HttpServlet() {
    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        var rest = RestClient(ScraperSelector())
        //res.writer.write(Gson().toJson(rest.getAllProductsOf(req.getRequestURI())))
        res.writer.write(Gson().toJson(getParams(req.getRequestURI(), "/web/")))
    }

    fun getParams(uri: String, urlBase: String): Map<String, String> {
        return uri.replaceFirst("$urlBase", "").split("/").toMap()
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
