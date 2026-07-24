package io.github.andreygomon.weather.data.error

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val error: ErrorType) : ResultWrapper<Nothing>()
}
