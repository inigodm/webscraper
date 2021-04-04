package inigo.repository

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import randomDataItem

class RepositoryManagerTest{
    lateinit var repo: RepositoryConnection
    lateinit var logger: Logger
    lateinit var sut: RepositoryManager

    @BeforeEach
    fun setup() {
        repo = mockk()
        logger = mockk()
        sut = RepositoryManager(repo, logger)
        MockKAnnotations.init(this)
    }

    @Test
     fun `item should be inserted if non existent`() {
        every { repo.findBy(any(), any())} returns emptyList()
        every { logger.trace(any()) } returns Unit
        every { repo.executePreparedStatement(any(), any()) } returns Unit
        val item = randomDataItem();

        sut.saveProductData(item)

        verify { repo.executePreparedStatement(any(), eq(item)) }
        verify { logger.trace("Inserting " + item.name) }
     }

    @Test
    fun `item should insert nothing if item has no name nor desc`() {
        every { repo.findBy(any(), any())} returns emptyList()
        every { logger.trace(any()) } returns Unit
        every { repo.executePreparedStatement(any(), any()) } returns Unit
        val item = randomDataItem();
        item.name = ""
        item.desc = ""

        sut.saveProductData(item)

        verify(exactly = 0) { repo.executePreparedStatement(any(), eq(item)) }
    }

    @Test
    fun `When item exists must be updated`() {
        val item = randomDataItem();
        val newItem = item.copy()
        every { repo.findBy(any(), any())} returns listOf(item)
        every { logger.trace(any()) } returns Unit
        every { repo.executeUpdate(any(), any()) } returns Unit

        sut.saveProductData(newItem)

        verify(exactly = 1) { repo.executeUpdate(any(), eq(item)) }
        verify { logger.trace("Updating " + item.name) }
    }

    @Test
    fun `Should not save items if they do not have name nor description`() {
        val item = randomDataItem()
        item.name = ""
        item.desc = ""

        every { repo.findBy(any(), any())} returns emptyList()
        every { logger.trace(any()) } returns Unit

        sut.saveProductData(item)

        verify (exactly = 0) { repo.executePreparedStatement(any(), any()) }
        verify (exactly = 0) { logger.trace(any()) }
    }
 }
