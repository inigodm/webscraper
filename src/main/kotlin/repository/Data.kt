package repository

data class ItemData(var name: String, var desc: String = "", var price: Int = -1, var extra: Map<String, Any> = mapOf(),
            var page: String = "", var type: String = "", var url: String = "")
