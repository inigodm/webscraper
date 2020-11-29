package inigo.repository

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.ZoneOffset

class RepositoryManager(
    var repo: RepositoryConnection,
    var logger: Logger = LoggerFactory.getLogger(RepositoryManager::class.java)) {

    fun saveProductData(itemData: ItemData) {
        val matchingItems = findMatchingItems(itemData)
        if (matchingItems.isEmpty()) {
            insertProduct(itemData)
        } else {
            updateProduct(itemData)
        }
    }
    
    private fun insertProduct(itemData: ItemData) {
        if (itemData.name.isEmpty() && itemData.desc.isEmpty()){
            return
        }
        logger.trace("Inserting " + itemData.name)
        repo.executePreparedStatement(
                "insert into products (name, desc, price, extra, page, type, active, created_at, last_updated_at, url)" +
                        " values (?, ?, ?, ?, ?, ?, 1, " +
                        "${dateAsLong()}," +
                        "${dateAsLong()}, ?)",
                itemData)
    }

    private fun updateProduct(itemData: ItemData) {
        logger.trace("Updating " + itemData.name)
        repo.executeUpdate(
                "update products set price = ?, extra = ?, " +
                        "active = 1, last_updated_at = ${dateAsLong()} " +
                        "where page = ? " +
                        "and type = ? " +
                        "and name = ? " +
                        "and desc = ?",
                itemData)
    }

    fun findMatchingItems(itemData: ItemData) : List<ItemData> {
        return repo.findBy2("""Select * from products where 
                page = ? 
                and name = ?
                and desc = ? 
                and price = ? 
                and url = ? """,
                listOf(itemData.page,
                    itemData.name,
                    itemData.desc,
                    itemData.price,
                    itemData.url)
                )

    }

    fun forgetProductsFromPageAndType(page: String, type: String = "") {
        val sql = when (type.isBlank()) {
            true -> "update products set active=false, last_updated_at = ${dateAsLong()} where page = '$page'"
            false -> "update products set active=false , last_updated_at = ${dateAsLong()} where page = '$page' and type = '$type'"
        }
        repo.executeCommand(sql)
    }

    private fun dateAsLong() = LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC)

    fun findProductsOf(page: String = "", type: String = "", query: String = ""): List<ItemData> {
        return when (query.isBlank()) {
            true -> findByPageAndPossiblyType(type, page)
            false -> findByPageAndQueryAndMaybeType(type, page, query)
        }
    }

    private fun findByPageAndQueryAndMaybeType(
        type: String,
        page: String,
        query: String
    ): List<ItemData> {
        return when (type.isBlank()) {
            true -> repo.findBy(
                "Select * from products where active = 1 and page = ? and lower(name) like ?",
                listOf(page, "%${query.toLowerCase()}%")
            )
            false -> repo.findBy(
                "Select * from products where active = 1 and page = ? and type = ? and lower(name) like ?",
                listOf(page, type, "%${query.toLowerCase()}%")
            )
        }
    }

    private fun findByPageAndPossiblyType(
        type: String,
        page: String
    ): List<ItemData> {
        return when (type.isBlank()) {
            true -> repo.findBy("Select * from products where active = 1 and page = ?", listOf(page))
            false -> repo.findBy("Select * from products where active = 1 and page = ? and type = ?", listOf(page, type))
        }
    }

    fun findNewProductsIn(page: String, type: String): List<ItemData> {
        return when(type.isBlank()){
            true -> repo.findBy("SELECT * FROM products WHERE active=1 and last_updated_at = (select max(last_updated_at) from products) and last_updated_at = created_at and page = ? order by type", listOf(page))
            false -> repo.findBy("SELECT * FROM products WHERE active=1 and last_updated_at = (select max(last_updated_at) from products) and last_updated_at = created_at and page = ? and type = ?", listOf(page, type))
        }
    }
}

fun main(args: Array<String>) {
    var conn = RepositoryConnection("scraper.db")
    conn.connect()
    conn.executeCommand(TABLE_PRODUCTS_CREATE)
    //val infoRetriever = RepositoryManager(conn)
    //logger.trace(infoRetriever.findProductsOf("ldlc", "any"))
    conn.close()
}
