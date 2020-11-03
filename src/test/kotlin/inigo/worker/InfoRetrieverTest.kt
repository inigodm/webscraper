package inigo.worker

import inigo.API.ScraperSelector
import assertk.assertThat
import assertk.assertions.isSameAs
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import randomResponse
import inigo.repository.RepositoryManager
import inigo.scraper.LDLCOportunitiesScrapper

class InfoRetrieverTest {
    lateinit var ldlcOportunitiesScrapper: LDLCOportunitiesScrapper
    lateinit var scraperSelector: ScraperSelector
    lateinit var repo : RepositoryManager
    lateinit var sut : InfoRetriever
    lateinit var repositoryManager: RepositoryManager

    @BeforeEach
    fun setUp(){
        scraperSelector = mockk()
        repo = mockk()
        ldlcOportunitiesScrapper = mockk()
        repositoryManager = mockk()
        sut = InfoRetriever(repo, scraperSelector)
    }

    @Test
    fun `should be able to call to a web page and retreive all info`() {
        val response = randomResponse()
        every { scraperSelector.findScraperFor("test")} returns ldlcOportunitiesScrapper
        every { ldlcOportunitiesScrapper.findData(any()) } returns response

        val info = sut.retrieveAllInfoFrom("test", "any")

        assertThat(info).isSameAs(response)
    }
}
