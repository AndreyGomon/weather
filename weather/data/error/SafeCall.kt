package ru.mascot.features.weather.data.error

import io.ktor.client.plugins.*
import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.coroutines.TimeoutCancellationException
import java.net.ConnectException
import java.net.UnknownHostException

suspend fun <T> safeCall(apiCall: suspend () -> T): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(apiCall())
    } catch (e: ResponseException) {
        val errorType = when (val statusCode = e.response.status.value) {
            400 -> ErrorType.BadRequest
            401 -> ErrorType.Unauthorized
            403 -> ErrorType.Forbidden
            404 -> ErrorType.NotFound
            else -> ErrorType.HttpError(statusCode, e.message)
        }
        ResultWrapper.Failure(errorType)
    } catch (e: UnknownHostException) {
        ResultWrapper.Failure(ErrorType.NetworkError)
    } catch (e: ConnectException) {
        ResultWrapper.Failure(ErrorType.NetworkError)
    } catch (e: SocketTimeoutException) {
        ResultWrapper.Failure(ErrorType.Timeout)
    } catch (e: TimeoutCancellationException) {
        ResultWrapper.Failure(ErrorType.Timeout)
    } catch (e: IllegalStateException) {
        ResultWrapper.Failure(ErrorType.NoData)
    } catch (e: Exception) {
        ResultWrapper.Failure(ErrorType.Unexpected(e))
    }
}