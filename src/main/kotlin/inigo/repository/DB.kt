package inigo.repository

import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

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
                        last_updated_at);
                        CREATE INDEX index_name 
                        ON products(name);
                        """
var TEST_INSERT = """insert into products (name, desc, price)
         values ('name', 'description', 21500)"""

class RepositoryConnection(dataBaseFile: String, var logger: Logger = LoggerFactory.getLogger(RepositoryConnection::class.java)) {
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
                logger.warn("The driver name is " + meta.driverName)
                logger.warn("A new database has been created.")
                conn!!.createStatement().use {
                    println("OK -> ${it.execute(TABLE_PRODUCTS_CREATE)}")
                }
            }
        } catch (e: SQLException) {
            logger.error(e.message, e)
        }
    }

    fun executeCommand(command: String) {
        if (!isConnected()){
            connect()
        }
        conn!!.createStatement().use {
            logger.trace("OK -> ${it.execute(command)}")
        }
    }

    fun executePreparedStatement(statement: String, data: ItemData) {
        if (!isConnected()){
            connect()
        }
        logProductInsert(statement, data)
        conn!!.prepareStatement(statement).use {
            it.setString(1, data.name)
            it.setString(2, data.desc)
            it.setInt(3, data.price)
            it.setString(4, Gson().toJson(data.extra))
            it.setString(5, data.page)
            it.setString(6, hashType(data.type))
            it.setString(7, data.url)
            println("OK -> ${it.executeUpdate()}")
        }
    }

    fun executeUpdate(sql: String, data: ItemData) {
        if (!isConnected()){
            connect()
        }
        conn!!.prepareStatement(sql).use {
            it.setInt(1, data.price)
            it.setString(2, Gson().toJson(data.extra))
            it.setString(3, data.page)
            it.setString(4, hashType(data.type))
            it.setString(5, data.name)
            it.setString(6, data.desc)
            println("OK -> ${it.executeUpdate()}")
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
