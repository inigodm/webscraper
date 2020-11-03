package inigo.shopscraper

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import inigo.exceptions.IncorrectNumberOfParams
import org.junit.jupiter.api.Test

class ScraperControllerKtTest{
    @Test
    fun `list must be converted to map correctly`() {
        assertThat(listOf("1", "2").toMap().get("1")).isEqualTo("2")
        assertThat(listOf("1", "2").toMap().get("2")).isEqualTo(null)
        assertThat(listOf(1, 2, 3, 4).toMap().get(3)).isEqualTo(4)
    }

    @Test
    fun `list toMap should throw a exception on non pair number of arguments`() {
        assertThat { listOf(1, 2, 3).toMap() }.isFailure().isInstanceOf(IncorrectNumberOfParams::class)
    }
}
