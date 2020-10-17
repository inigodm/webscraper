import com.github.javafaker.Faker
import repository.ItemData

fun randomDataItem(): ItemData {
    val faker = Faker()
    return ItemData(
        faker.ancient().god(),
        faker.rickAndMorty().quote(),
        faker.number().numberBetween(0, 1000000),
        mapOf(
            faker.backToTheFuture().quote()
                    to faker.funnyName().name()
        ),
        faker.internet().url()
    )
}

fun randomResponse(): MutableMap<String, List<ItemData>> {
    val faker = Faker()
    val res = mutableMapOf<String, List<ItemData>>()
    for (numCats in 0 until faker.number().numberBetween(0, 10)) {
        val category = faker.artist().name()
        val products = mutableListOf<ItemData>()
        for (numProds in 0 until faker.number().numberBetween(0, 10)) {
            products.add(randomDataItem())
        }
        res.put(category, products)
    }
    return res
}
