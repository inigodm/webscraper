import com.github.javafaker.Faker
import repository.ItemData

fun randomDataItem(): ItemData {
    var faker = Faker()
    return ItemData(
        faker.ancient().god(),
        faker.rickAndMorty().quote(),
        faker.number().numberBetween(0, 1000000),
        mapOf(
            faker.backToTheFuture().quote()
                    to faker.funnyName().name()
        )
    )
}

fun randomResponse(): MutableMap<String, List<ItemData>> {
    var faker = Faker()
    val res = mutableMapOf<String, List<ItemData>>()
    for (numCats in 0 until faker.number().numberBetween(0, 10)) {
        var category = faker.artist().name()
        var products = mutableListOf<ItemData>()
        for (numProds in 0 until faker.number().numberBetween(0, 10)) {
            products.add(randomDataItem())
        }
        res.put(category, products)
    }
    return res
}
