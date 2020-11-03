package inigo.exceptions

open class APIException(message: String) : RuntimeException(message)
class ScraperNotFound(message: String): APIException("Scraper with id `$message` not found")
class IncorrectNumberOfParams(message: String): APIException(message) {
    companion object{
        @JvmStatic
        fun becauseMustBePair() = IncorrectNumberOfParams("Number of params must be multiple of 2")
    }
}

