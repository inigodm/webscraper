import inigo.API.ScraperSelector
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import inigo.scraper.LDLCOportunitiesScrapper

class APITest{
    lateinit var scraperSelector: ScraperSelector
    lateinit var ldlcOportunitiesScrapper: LDLCOportunitiesScrapper

    @BeforeEach
    fun setUp(){
        ldlcOportunitiesScrapper = mockk()
        scraperSelector = ScraperSelector(mapOf("ldlc" to ldlcOportunitiesScrapper))
    }

    /*@Test
    fun `should return all products for given page`() {
        client = RestClient(scraperSelector)
        var response = randomResponse()
        every { ldlcOportunitiesScrapper.findData() } returns response

        val res = client.getAllProductsOf("ldlc")

        assertThat(res).isEqualTo(response)
    }*/
}
