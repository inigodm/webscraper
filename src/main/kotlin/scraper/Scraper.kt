package scraper

interface Scraper {
    fun findData(): Map<String, List<ItemData>>
}
