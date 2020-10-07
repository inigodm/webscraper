package worker

import API.ScraperSelector
import repository.RepositoryConnection
import repository.RepositoryManager
import repository.TABLE_PRODUCTS_CREATE
import scraper.ItemData

class InfoRetriever(val repo: RepositoryManager, val scraperSelector: ScraperSelector) {
    fun retrieveAllInfoFrom(page: String, type: String) = scraperSelector.findScraperFor(page).findData(type)
    fun updateProductDataForPage(page: String, type: String) {
        repo.forgetProductsFromPageAndType(page, type)
        save(page, retrieveAllInfoFrom(page, type))
    }
    fun save(page: String, response: Map<String, List<ItemData>>) {
        response.map { (k, v) ->
            v.forEach{
                it.type = k
                it.page = page
                repo.saveProductData(it)
            }
        }
    }
}


fun main(args: Array<String>) {
    var conn = RepositoryConnection("scraper.db")
    conn.connect()
    conn.executeCommand(TABLE_PRODUCTS_CREATE)
    val infoRetriever = InfoRetriever(RepositoryManager(conn), ScraperSelector())
    infoRetriever.updateProductDataForPage("ldlc", "Disco SSD")
    conn.close()
}


