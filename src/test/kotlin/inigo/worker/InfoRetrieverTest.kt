package inigo.worker

import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import inigo.repository.RepositoryManager
import inigo.scraper.LDLCOportunitiesScrapper
import io.mockk.verify

class InfoRetrieverTest {
    lateinit var ldlcOportunitiesScrapper: LDLCOportunitiesScrapper
    lateinit var repo : RepositoryManager
    lateinit var sut : InfoRetriever
    lateinit var repositoryManager: RepositoryManager

    @BeforeEach
    fun setUp(){
        repo = mockk()
        ldlcOportunitiesScrapper = mockk()
        repositoryManager = mockk()
        sut = InfoRetriever(repo)
    }

    @Test
    fun `should be able to call to a web page and retreive all info`() {
        sut.updateProductDataForPage("any")
        verify { ldlcOportunitiesScrapper.updateData("any") }
    }
}
