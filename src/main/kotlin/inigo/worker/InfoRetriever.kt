package inigo.worker

import inigo.repository.RepositoryConnection
import inigo.repository.RepositoryManager
import inigo.repository.TABLE_PRODUCTS_CREATE
import inigo.scraper.LDLCOportunitiesScrapper

class InfoRetriever(var webScraper: LDLCOportunitiesScrapper) {
    private val types = mapOf(
        "tarjetagrfica" to "Tarjeta Gráfica",
        "tv" to "TV",
        "pantalla" to "Pantalla PC",
        "memoriapc" to "Memoria PC",
        "tablet" to "Tablet",
        "cmaradefotos" to "Cámara de fotos",
        "auriculares" to "Auriculares",
        "discossd" to "Disco SSD",
        "discodurointerno" to "Disco duro interno",
        "procesador" to "Procesador",
        "impresoras" to "Impresora",
        "mvilysmartphone" to "Smartphones",
    )

    fun updateProductDataForPage(type: String) {
        webScraper.updateData(types[type] ?: "any")
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


