import com.github.javafaker.Faker
import inigo.repository.ItemData

fun randomDataItem(): ItemData {
    val faker = Faker()
    return ItemData(
        name = faker.ancient().god(),
        desc = faker.rickAndMorty().quote(),
        price = faker.number().numberBetween(0, 1000000),
        extra = mutableMapOf(
            faker.backToTheFuture().quote()
                    to faker.funnyName().name()
        ),
        type = faker.princessBride().character(),
        page = faker.gameOfThrones().character(),
        url = faker.internet().url()
    )
}

fun randomResponse(): List<ItemData> {
    val faker = Faker()
    val res = mutableListOf<ItemData>()
    for (numCats in 0 until faker.number().numberBetween(0, 10)) {
        res.add(randomDataItem())
    }
    return res
}
