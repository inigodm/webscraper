package repository

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*
import java.util.stream.Collectors
import java.util.stream.StreamSupport

val PATH = "/home/inigo/projects/ShopScraper"

var TABLE_PRODUCTS_CREATE = """CREATE TABLE IF NOT EXISTS products
                        (id integer PRIMARY KEY,
                        name TEXT NOT NULL,
                        desc TEXT,
                        price integer,
                        extra TEXT,
                        picture BLOB,
                        page TEXT,
                        type TEXT,
                        url TEXT,
                        active integer,
                        created_at integer,
                        last_updated_at)"""
var TEST_INSERT = """insert into products (name, desc, price)
         values ('name', 'description', 21500)"""

class RepositoryConnection(dataBaseFile: String) {
    val DB_PATH = "jdbc:sqlite:$PATH/sqlite/"
    var conn: Connection? = null
    val URL : String = "$DB_PATH$dataBaseFile"

    init{
        Class.forName("org.sqlite.JDBC")
    }

    fun connect() {
        try {
            conn = DriverManager.getConnection(URL)
            if (conn != null) {
                val meta = conn!!.metaData
                println("The driver name is " + meta.driverName)
                println("A new database has been created.")
            }
        } catch (e: SQLException) {
            println(e.message)
        }
    }

    fun findProducts(query: String) : List<ItemData>? {
        println("Executing: $query")
        DriverManager.getConnection(URL).use { conn ->
            val resultSet = conn?.prepareStatement(query)?.executeQuery()
            val gson = Gson()
            val res = mutableListOf<ItemData>()
            while (resultSet!!.next()) {
                with(resultSet) {
                    res.add(
                        ItemData(name = resultSet.getString("name"),
                            desc = resultSet.getString("desc"),
                            price = resultSet.getInt("price"),
                            extra = gson.fromJson(resultSet.getString("extra"), MapParametrizedType()),
                            page = resultSet.getString("page"),
                            type = resultSet.getString("type"))
                    )
                }
            }
            return res
        }
    }

    fun <T> toArrayList(iterator: Iterator<T>?): List<T>? {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                .collect(Collectors.toCollection { ArrayList() })
    }


    fun executeCommand(command: String) {
        if (!isConnected()){
            connect()
        }
        println("Executing: $command")
        var statement = conn!!.createStatement()
        statement.use {
            var res = statement.execute(command)
            println("OK -> $res")
        }
    }

    fun executePreparedStatement(statement: String, data: ItemData) {
        if (!isConnected()){
            connect()
        }
        logProductInsert(statement, data)
        val statement = conn!!.prepareStatement(statement)
        statement.use {
            statement.setString(1, data.name)
            statement.setString(2, data.desc)
            statement.setInt(3, data.price)
            statement.setString(4, Gson().toJson(data.extra))
            statement.setString(5, data.page)
            statement.setString(6, hashType(data.type))
            statement.setString(7, data.url)
            println("OK -> ${statement.executeUpdate()}")
        }
    }

    fun executeUpdate(sql: String, data: ItemData) {
        if (!isConnected()){
            connect()
        }
        val statement = conn!!.prepareStatement(sql)
        statement.use {
            statement.setInt(1, data.price)
            statement.setString(2, Gson().toJson(data.extra))
            statement.setString(3, data.page)
            statement.setString(4, hashType(data.type))
            statement.setString(5, data.name)
            statement.setString(6, data.desc)
            println("OK -> ${statement.executeUpdate()}")
        }
    }

    private fun logProductInsert(statement: String, data: ItemData) {
        var sql = statement.replaceFirst("?", "'${data.name}'")
        sql = sql.replaceFirst("?", "'${data.desc}'" ?: "")
        sql = sql.replaceFirst("?", data.price.toString())
        sql = sql.replaceFirst("?", "'${Gson().toJson(data.extra)}'")
        sql = sql.replaceFirst("?", "'${data.page}'")
        sql = sql.replaceFirst("?", "'${hashType(data.type)}'" ?: "")
        sql = sql.replaceFirst("?", "'${data.url}'" ?: "")
        println(sql)
    }

    fun close() {
        if (isConnected()){
            conn!!.close()
        }
    }

    private fun isConnected() = conn != null && !conn!!.isClosed()

    fun findBy(sql: String, vars: List<String>): List<ItemData> {
        println("Executing: $sql")
        DriverManager.getConnection(URL).use { conn ->
            val statement = conn?.prepareStatement(sql)!!
            val gson = Gson()
            val res = mutableListOf<ItemData>()
            var i = 1
            statement.use {
                vars.forEach{
                    statement.setString(i, it)
                    i++
                }
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    with(resultSet) {
                        res.add(
                            ItemData(name = resultSet.getString("name"),
                                desc = resultSet.getString("desc"),
                                price = resultSet.getInt("price"),
                                extra = gson.fromJson(resultSet.getString("extra"), MapParametrizedType()),
                                page = resultSet.getString("page"),
                                type = resultSet.getString("type"),
                                url = resultSet.getString("url"))
                        )
                    }
                }
            }
            return res
        }
    }
    private fun hashType(type: String): String {
        return type.replace("[^0-9^a-z^A-Z]".toRegex(), "").toLowerCase()
    }
}

fun main(args: Array<String>) {
    var conn = RepositoryConnection("test.db")
    conn.connect()
    conn.executeCommand(TABLE_PRODUCTS_CREATE)
    conn.executeCommand(TEST_INSERT)
    conn.close()
}

class MapParametrizedType: ParameterizedType{

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf<Type>(String::class.java, Any::class.java)
    }
    override fun getRawType(): Type {
        return Map::class.java
    }
    override fun getOwnerType(): Type? {
        return null
    }

}
