package inigo.common

import java.io.IOException
import java.net.MalformedURLException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

fun <R> throwsServiceException(block: () -> R): R {
    try {
        return block()
    } catch (e: MalformedURLException) {
        throw ServiceException(e.message ?: "", e)
    } catch (e: UnknownHostException) {
        throw ServiceException(e.message ?: "", e)
    } catch (e: SSLHandshakeException) {
        throw ServiceException(e.message ?: "", e)
    } catch (e: IOException) {
        throw ServiceException(e.message ?: "", e)
    } catch (e: IllegalArgumentException) {
        throw ServiceException(e.message ?: "", e)
    }
}

class ServiceException(message: String, ex: Exception) : Exception(ex) {}
