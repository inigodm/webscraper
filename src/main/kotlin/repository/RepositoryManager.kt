package repository

import scraper.ItemData

class RepositoryManager(var repo: RepositoryConnection) {

    fun saveProductData(itemData: ItemData) {
        repo.executePreparedStatement(
                "insert into products (name, desc, price, extra, page, type, active, created_at)" +
                        " values (?, ?, ?, ?, ?, ?, 1, ${System.currentTimeMillis()})",
        itemData)
    }
}
