package inigo.scraper

import inigo.cardPage
import inigo.repository.RepositoryManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import inigo.mainpage
import io.mockk.verify
import org.slf4j.Logger

internal class LDLCOportunitiesScrapperTest {
    val repo: RepositoryManager = mockk()
    val root: String = "https://www.ldlc.com/es-es/n2193/oportunidades/"
    val logger: Logger = mockk()
    val sut : LDLCOportunitiesScrapper = spyk(LDLCOportunitiesScrapper(repo, root, 4, logger))
    @BeforeEach
    fun setup() {
    }

    @Test
    fun `should update given product type`() {
        every { repo.forgetProductsFromPageAndType(any(), any()) } returns Unit
        every { sut.getHtmlDocument(root) } returns mainpage().mainpageDoc
        every { logger.trace(any()) } returns Unit
        every { repo.saveProductData(any()) } returns Unit

        sut.updateData("Tarjeta gráfica")

        verify { logger.trace("Added to search https://www.ldlc.com/es-es/oportunidades/c4684/") }
        //verify { logger.trace("Skipping Pantalla PC we are searching for Tarjeta Gráfica")}
    }
}
