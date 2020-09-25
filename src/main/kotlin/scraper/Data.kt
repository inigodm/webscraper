package scraper

data class IzenData(var izena: String, var esanahia: String = "")
data class ItemData(var name: String, var desc: String = "", var price: Int = -1, var extra: Map<String, Any> = mapOf())
