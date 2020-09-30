package repository

import scraper.ItemData

class RepositoryManager(var repo: RepositoryConnection) {

    fun saveProductData(itemData: ItemData) {
        repo.executePreparedStatement(
                "insert into products (name, desc, price, extra, page, type, active, created_at)" +
                        " values (?, ?, ?, ?, ?, ?, 1, ${System.currentTimeMillis()})",
        itemData)
    }

    fun forgetProductsFromPage(page: String) {
        repo.executeCommand("delete from products where page = '$page'")
    }

    fun findProductsOf(page: String = ""): List<ItemData> {
        return repo.findProducts("Select * from products where page = '$page'") ?: listOf()
    }
}

fun main(args: Array<String>) {
    var conn = RepositoryConnection("scraper.db")
    conn.connect()
    conn.executeCommand(TABLE_PRODUCTS_CREATE)
    val infoRetriever = RepositoryManager(conn)
    println(infoRetriever.findProductsOf("ldlc"))
    conn.close()
}
