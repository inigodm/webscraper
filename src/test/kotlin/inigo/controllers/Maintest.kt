package inigo.controllers

import inigo.repository.RepositoryManager
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class Maintest {
    lateinit var scraperController : ScraperController
    val logger: Logger = mockk()
    val repo: RepositoryManager = mockk()
    val req: HttpServletRequest = mockk()
    val res: HttpServletResponse = mockk()

    @BeforeEach
    fun setup() {
        scraperController = ScraperController(repo, logger)
    }

    @Test
    fun `should ask for a update of given product`() {
        //scraperController.doPut(req, res)
    }
}
