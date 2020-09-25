import API.RestClient
import API.ScraperSelector
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.javafaker.Faker
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import scraper.ItemData
import scraper.LDLCOportunitiesScrapper

class APITest{
    lateinit var client : RestClient
    lateinit var scraperSelector: ScraperSelector
    lateinit var ldlcOportunitiesScrapper: LDLCOportunitiesScrapper

    @BeforeEach
    fun setUp(){
        ldlcOportunitiesScrapper = mockk()
        scraperSelector = ScraperSelector(mapOf("ldlc" to ldlcOportunitiesScrapper))
    }

    @Test
    fun `should return all products for given page`() {
        client = RestClient(scraperSelector)
        var response = randomResponse()
        every { ldlcOportunitiesScrapper.findData() } returns response

        val res = client.getAllProductsOf("ldlc")

        assertThat(res).isEqualTo(response)
    }
}
