import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach

class Download_is_ok_if{
    @MockK lateinit var categories : Connection
    val url = "https://www.ldlc.com/es-es/n2193/oportunidades/"

    @BeforeEach
    fun setUp() {
        mockkStatic(Jsoup::class)
        every { Jsoup.connect(url) } returns categories
        every { categories.userAgent(any()) } returns categories
        every { categories.timeout(any()) } returns categories
    }
}
