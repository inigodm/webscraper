package exceptions

open class APIException(message: String) : RuntimeException(message)
class ScraperNotFound(message: String): APIException("Scraper with id `$message` not found")
