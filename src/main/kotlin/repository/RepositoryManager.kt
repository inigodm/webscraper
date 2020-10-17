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
        if (itemData.name.isEmpty() && itemData.desc.isEmpty()){
            return
        }
        repo.executePreparedStatement(
                "insert into products (name, desc, price, extra, page, type, active, created_at, last_updated_at, url)" +
                        " values (?, ?, ?, ?, ?, ?, 1, " +
                        "${dateAsLong()}," +
                        "${dateAsLong()}, ?)",
                itemData)
    }

    private fun updateProduct(itemData: ItemData) {
        println("Updating " + itemData.name)
        repo.executeUpdate(
                "update products set price = ?, extra = ?, " +
                        "active = 1, last_updated_at = ${dateAsLong()} " +
                        "where page = ? " +
                        "and type = ? " +
                        "and name = ? " +
                        "and desc = ?",
                itemData)
    }

    fun nonexitentProduct(itemData: ItemData) : Boolean {
        return repo.findBy("Select * from products where " +
                "page = ? " +
                " and name = ?" +
                " and desc = ?", listOf(itemData.page, itemData.name, itemData.desc)).isNullOrEmpty()

    }

    fun forgetProductsFromPageAndType(page: String, type: String = "") {
        val sql = when (type.isBlank()) {
            true -> "update products set active=false, last_updated_at = ${dateAsLong()} where page = '$page'"
            false -> "update products set active=false , last_updated_at = ${dateAsLong()} where page = '$page' and type = '$type'"
        }
        repo.executeCommand(sql)
    }

    private fun dateAsLong() = LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC)

    fun findProductsOf(page: String = "", type: String = ""): List<ItemData> {
        return when (type.isBlank()) {
            true -> repo.findBy("Select * from products where page = ?", listOf(page))
            false -> repo.findBy("Select * from products where page = ? and type = ?", listOf(page, type))
        }
    }
}

fun main(args: Array<String>) {
    var conn = RepositoryConnection("scraper.db")
    conn.connect()
    conn.executeCommand(TABLE_PRODUCTS_CREATE)
    val infoRetriever = RepositoryManager(conn)
    println(infoRetriever.findProductsOf("ldlc", "any"))
    conn.close()
}
