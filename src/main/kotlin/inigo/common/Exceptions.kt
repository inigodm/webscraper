package inigo.common

import java.io.IOException
import java.net.MalformedURLException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

fun <R> throwsServiceException(url: String, block: () -> R): R {
    try {
        return block()
    } catch (e: MalformedURLException) {
        throw ServiceException("trying to access $url ${e.message}", e)
    } catch (e: UnknownHostException) {
        throw ServiceException("trying to access $url ${e.message}", e)
    } catch (e: SSLHandshakeException) {
        throw ServiceException("trying to access $url ${e.message}", e)
    } catch (e: IOException) {
        throw ServiceException("trying to access $url ${e.message}", e)
    } catch (e: IllegalArgumentException) {
        throw ServiceException("trying to access $url ${e.message}", e)
    }
}

class ServiceException(message: String, ex: Exception) : Exception(ex) {}
