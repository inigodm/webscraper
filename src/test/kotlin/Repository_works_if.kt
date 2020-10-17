import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.RepositoryConnection
import repository.RepositoryManager

class RepositoryManagerTest{
    lateinit var connection : RepositoryConnection
    lateinit var repo : RepositoryManager

    @BeforeEach
    fun setUp() {
        connection = mockk()
        repo = RepositoryManager(connection)
    }

    @Test
    fun `inserts the product`() {
        var itemData = randomDataItem()
        every { connection.executePreparedStatement(any(), any()) } returns Unit
        every { connection.findBy(any(), any()) } returns emptyList()

        repo.saveProductData(itemData)

        verify {
            connection.executePreparedStatement(any(), eq(itemData))
        }
    }

    @Test
    fun `updates the product`() {
        var itemData = randomDataItem()
        every { connection.executeUpdate(any(), any()) } returns Unit
        every { connection.findBy(any(), any()) } returns listOf(randomDataItem())

        repo.saveProductData(itemData)

        verify {
            connection.executeUpdate(any(), eq(itemData))
        }
    }
}
