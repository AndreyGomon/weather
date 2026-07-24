package io.github.andreygomon.weather.data.error

sealed class ErrorType(
    val message: String
) {
    data class HttpError(val code: Int, val exception: String?) : ErrorType("HTTP $code: $exception")
    data object BadRequest : ErrorType("Некорректный запрос")
    data object Unauthorized : ErrorType("Требуется авторизация")
    data object Forbidden : ErrorType("Доступ запрещен")
    data object NotFound : ErrorType("Данные не найдены")
    data object NetworkError : ErrorType("Нет сети")
    data object Timeout : ErrorType("Превышено время ожидания")
    data object NoData: ErrorType("Нет данных")
    data class Unexpected(val error: Throwable) : ErrorType("Неизвестная ошибка")

    override fun toString(): String = message
}