package worker

import API.ScraperSelector
import repository.RepositoryManager
import scraper.ItemData

class InfoRetriever(val repo: RepositoryManager, val scraperSelector: ScraperSelector) {
    fun retrieveAllInfoFrom(page: String) = scraperSelector.findScraperFor(page).findData()
    fun save(page: String, response: MutableMap<String, List<ItemData>>) {
        response.map { (k, v) ->
            v.forEach{
                it.type = k
                it.page = page
                repo.saveProductData(it)
            }
        }
    }
}
