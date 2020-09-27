package API

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import scraper.LDLCOportunitiesScrapper

class ScraperSelectorTest {

    @Test
    fun `should return scraper for ldcl`() {
        val sut = ScraperSelector()

        val res = sut.findScraperFor("ldlc")

        assertThat(res).isInstanceOf(LDLCOportunitiesScrapper::class)
    }

    @Test
    fun `should throw a not found exception when key is unknown`() {
        val sut = ScraperSelector()

        assertThat { sut.findScraperFor("non existent") }
            .isFailure()
            .hasMessage("Scraper with id `non existent` not found")

    }
}
