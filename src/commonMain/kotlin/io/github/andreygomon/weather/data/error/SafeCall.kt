package io.github.andreygomon.weather.data.error

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException

suspend fun <T> safeCall(apiCall: suspend () -> T): ResultWrapper<T> = try {
    ResultWrapper.Success(apiCall())
} catch (error: ResponseException) {
    val statusCode = error.response.status.value
    val type = when (statusCode) {
        400 -> ErrorType.BadRequest
        401 -> ErrorType.Unauthorized
        403 -> ErrorType.Forbidden
        404 -> ErrorType.NotFound
        else -> ErrorType.HttpError(statusCode, error.message)
    }
    ResultWrapper.Failure(type)
} catch (error: HttpRequestTimeoutException) {
    ResultWrapper.Failure(ErrorType.Timeout)
} catch (error: ConnectTimeoutException) {
    ResultWrapper.Failure(ErrorType.Timeout)
} catch (error: SocketTimeoutException) {
    ResultWrapper.Failure(ErrorType.Timeout)
} catch (error: TimeoutCancellationException) {
    ResultWrapper.Failure(ErrorType.Timeout)
} catch (error: CancellationException) {
    throw error
} catch (error: IOException) {
    ResultWrapper.Failure(ErrorType.NetworkError)
} catch (error: IllegalStateException) {
    ResultWrapper.Failure(ErrorType.NoData)
} catch (error: Throwable) {
    ResultWrapper.Failure(ErrorType.Unexpected(error))
}
