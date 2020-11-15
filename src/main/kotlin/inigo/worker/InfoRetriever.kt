package inigo.worker

import inigo.repository.RepositoryConnection
import inigo.repository.RepositoryManager
import inigo.repository.TABLE_PRODUCTS_CREATE
import inigo.scraper.LDLCOportunitiesScrapper

class InfoRetriever(var webScraper: LDLCOportunitiesScrapper) {
    fun updateProductDataForPage(type: String) {
        webScraper.updateData(type)
    }

}


fun main(args: Array<String>) {
    var conn = RepositoryConnection("scraper.db")
    conn.connect()
    conn.executeCommand(TABLE_PRODUCTS_CREATE)
    val infoRetriever = InfoRetriever(LDLCOportunitiesScrapper(RepositoryManager(conn)))
    infoRetriever.updateProductDataForPage("any")
    conn.close()
}


