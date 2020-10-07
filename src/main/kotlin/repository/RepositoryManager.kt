package repository

import scraper.ItemData
import java.time.LocalDate
import java.time.ZoneOffset

class RepositoryManager(var repo: RepositoryConnection) {

    fun saveProductData(itemData: ItemData) {
        if (existProduct(itemData)) {
            updateProduct(itemData)
        } else {
            insertProduct(itemData)
        }
    }

    private fun insertProduct(itemData: ItemData) {
        println("Inserting " + itemData.name)
        repo.executePreparedStatement(
                "insert into products (name, desc, price, extra, page, type, active, created_at)" +
                        " values (?, ?, ?, ?, ?, ?, 1, ${LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC)})",
                itemData)
    }

    private fun updateProduct(itemData: ItemData) {
        println("Updating " + itemData.name)
        repo.executePreparedStatement(
                "update products set name = ?, desc = ?, price = ?, extra = ?, page = ?, type = ?, " +
                        "active = 1, created_at = ${LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC)}",
                itemData)
    }

    fun existProduct(itemData: ItemData) : Boolean {
        return repo.findProducts("Select * from products where " +
                "page = '${itemData.page}' " +
                "and name = '${itemData.name}' " +
                "and price =${itemData.price} " +
                "and desc = '${itemData.desc}'").isNullOrEmpty()

    }

    fun forgetProductsFromPageAndType(page: String, type: String = "") {
        val sql = when (type.isBlank()) {
            true -> "update products set active=false where page = '$page'"
            false -> "update products set active=false where page = '$page' and type = '$type'"
        }
        repo.executeCommand(sql)
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
