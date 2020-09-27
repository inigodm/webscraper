package repository

import scraper.ItemData

class RepositoryManager(var repo: RepositoryConnection) {

    fun saveProductData(itemData: ItemData) {
        repo.executePreparedStatement(
                "insert into products (name, desc, price, extra, page, type, active, created_at)" +
                        " values (?, ?, ?, ?, ?, ?, 1, ${System.currentTimeMillis()})",
        itemData)
    }

    fun forgetProducts() {
        repo.executeCommand("delete from products where 1=1")
    }

    fun forgetProductsFromPage(page: String) {
        repo.executeCommand("delete from products where page = '$page'")
    }
}
