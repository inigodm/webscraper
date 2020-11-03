package inigo.API

import inigo.exceptions.ScraperNotFound
import inigo.scraper.LDLCOportunitiesScrapper
import inigo.scraper.WebScrapper

class ScraperSelector(var scrapers: Map<String, WebScrapper>
                      = mapOf<String, WebScrapper>("ldlc" to LDLCOportunitiesScrapper())){

    fun findScraperFor(scraperKey: String): WebScrapper {
        return scrapers.get(scraperKey) ?: throw ScraperNotFound(scraperKey)
    }
}
