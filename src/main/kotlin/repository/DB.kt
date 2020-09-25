package repository

import com.google.gson.Gson
import scraper.ItemData
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

var TABLE_PRODUCTS_CREATE = """CREATE TABLE IF NOT EXISTS products
                        (id integer PRIMARY KEY,
                        name TEXT NOT NULL,
                        desc TEXT,
                        price integer,
                        extra TEXT,
                        picture BLOB)"""
var TEST_INSERT = """insert into products (name, desc, price)
         values ('name', 'description', 21500)"""

class RepositoryConnection(dataBaseFile : String) {
    val DB_PATH = "jdbc:sqlite:./sqlite/"
    var conn: Connection? = null
    val URL : String = "$DB_PATH$dataBaseFile"

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

    fun <T> executeQuery(query: String) : T? {
        return null
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
        var sql = statement.replaceFirst("?", data.name)
        sql = sql.replaceFirst("?", data.desc)
        sql = sql.replaceFirst("?", data.price.toString())
        sql = sql.replaceFirst("?", Gson().toJson(data.extra))
        sql = sql.replaceFirst("?", data.desc)
        println(sql)
        var statement = conn!!.prepareStatement(statement)
        statement.use {
            statement.setString(1, data.name)
            statement.setString(2, data.desc)
            statement.setInt(3, data.price)
            statement.setString(4, Gson().toJson(data.extra))
            var res = statement.executeUpdate()
            println("OK -> $res")
        }
    }

    fun close() {
        if (isConnected()){
            conn!!.close()
        }
    }

    private fun isConnected() = conn != null && !conn!!.isClosed()
}

fun main(args: Array<String>) {
    var conn = RepositoryConnection("test.db")
    conn.connect()
    conn.executeCommand(TABLE_PRODUCTS_CREATE)
    conn.executeCommand(TEST_INSERT)
    conn.close()
}
