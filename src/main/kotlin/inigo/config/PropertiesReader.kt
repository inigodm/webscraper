package inigo.config

import java.io.FileInputStream
import java.util.*

class PropertiesReader {
    companion object Builder {
        var inner : Properties = Properties()

        fun getProperties(): Properties{
            if (inner.isEmpty){
                inner.load(FileInputStream("/home/inigo/config/scraper.properties"))
            }
            return inner
        }
    }
}
