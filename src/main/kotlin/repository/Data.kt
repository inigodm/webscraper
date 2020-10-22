package repository

data class ItemData(var name: String, var desc: String = "", var price: Int = -1, var extra: MutableMap<String, Any> = mutableMapOf(),
            var page: String = "", var type: String = "", var url: String = "")
