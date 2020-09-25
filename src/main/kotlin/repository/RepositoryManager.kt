package repository

import scraper.ItemData

class RepositoryManager(var repo: RepositoryConnection) {

    fun saveProductData(itemData: ItemData) {
        repo.executePreparedStatement(
                "insert into products (name, desc, price, extra) values (?, ?, ?, ?)",
        itemData)
    }
}
