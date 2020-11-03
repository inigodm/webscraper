package inigo.scraper

import inigo.repository.ItemData

interface Scraper {
    fun findData(): Map<String, List<ItemData>>
}
