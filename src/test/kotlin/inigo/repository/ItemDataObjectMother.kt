package inigo.repository

import com.github.javafaker.Faker
import org.junit.jupiter.api.Assertions.*

class ItemDataObjectMother {
    fun randomItemData() : ItemData{
        val faker = Faker()
        return ItemData(
                faker.gameOfThrones().character(),
                faker.princessBride().quote(),
                faker.number().numberBetween(100, 10000),
                mutableMapOf("one" to "two"),
                faker.ancient().hero(),
                faker.ancient().god(),
                faker.internet().url())
    }
}
