package worker

import API.ScraperSelector
import assertk.assertThat
import assertk.assertions.isSameAs
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import randomResponse
import repository.RepositoryConnection
import repository.RepositoryManager
import repository.TABLE_PRODUCTS_CREATE
import scraper.LDLCOportunitiesScrapper

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
        every { ldlcOportunitiesScrapper.findData() } returns response

        val info = sut.retrieveAllInfoFrom("test")

        assertThat(info).isSameAs(response)
    }

    @Test
    fun `should be able to save all info in a repository`() {
        val response = randomResponse()
        every { repositoryManager.saveProductData(any()) }
        var conn = RepositoryConnection("test.db")
        conn.connect()
        conn.executeCommand(TABLE_PRODUCTS_CREATE)
        sut = InfoRetriever(RepositoryManager(conn), scraperSelector)

        sut.save("test", response)

        assertThat {  }
    }
}
