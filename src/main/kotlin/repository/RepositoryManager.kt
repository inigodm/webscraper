package repository

import java.time.LocalDate
import java.time.ZoneOffset

class RepositoryManager(var repo: RepositoryConnection) {

    fun saveProductData(itemData: ItemData) {
        if (nonexitentProduct(itemData)) {
            insertProduct(itemData)
        } else {
            updateProduct(itemData)
        }
    }

    private fun insertProduct(itemData: ItemData) {
        println("Inserting " + itemData.name)
        repo.executePreparedStatement(
                "insert into products (name, desc, price, extra, page, type, active, created_at, last_updated_at)" +
                        " values (?, ?, ?, ?, ?, ?, 1, " +
                        "${dateAsLong()}," +
                        "${dateAsLong()})",
                itemData)
    }

    private fun updateProduct(itemData: ItemData) {
        println("Updating " + itemData.name)
        repo.executePreparedStatement(
                "update products set name = ?, desc = ?, price = ?, extra = ?, page = ?, type = ?, " +
                        "active = 1, last_updated_at = ${dateAsLong()}",
                itemData)
    }

    fun nonexitentProduct(itemData: ItemData) : Boolean {
        return repo.findProducts("Select * from products where " +
                "page = '${itemData.page.replace('\'', '"')}' " +
                "and name = '${itemData.name.replace('\'', '"')}' " +
                "and price =${itemData.price} " +
                "and desc = '${itemData.desc.replace('\'', '"')}'").isNullOrEmpty()

    }

    fun forgetProductsFromPageAndType(page: String, type: String = "") {
        val sql = when (type.isBlank()) {
            true -> "update products set active=false, last_updated_at = ${dateAsLong()} where page = '$page'"
            false -> "update products set active=false , last_updated_at = ${dateAsLong()} where page = '$page' and type = '$type'"
        }
        repo.executeCommand(sql)
    }

    private fun dateAsLong() = LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC)

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
