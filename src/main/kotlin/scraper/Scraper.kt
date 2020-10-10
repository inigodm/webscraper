package scraper

import repository.ItemData

interface Scraper {
    fun findData(): Map<String, List<ItemData>>
}
