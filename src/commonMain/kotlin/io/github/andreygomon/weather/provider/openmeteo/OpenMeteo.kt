package io.github.andreygomon.weather.provider.openmeteo

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import io.ktor.http.path
import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.provider.WeatherClient
import io.github.andreygomon.weather.provider.WeatherProvider

class OpenMeteoProvider : WeatherProvider {
    companion object {
        const val NAME = "OpenMeteo"
    }

    override val name = NAME
    override val url = "https://api.open-meteo.com/"

    override val perMinute: Long
        get() = OpenMeteoRequests.PER_MINUTE.maxRequests
    override val perHour: Long
        get() = OpenMeteoRequests.PER_HOUR.maxRequests
    override val perDay: Long
        get() = OpenMeteoRequests.PER_DAY.maxRequests

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): WeatherData =
        request<OpenMeteoCurrentResponse>(latitude, longitude) {
            current(OpenMeteoCurrentParameters.parameters)
        }.mapResponse("Failed to get current weather data from OpenMeteo") {
            it.currentWeather?.toWeatherData(latitude, longitude, name = NAME)
        }

    override suspend fun getForecastWeather(
        latitude: Double,
        longitude: Double
    ): ForecastList =
        request<OpenMeteoForecastResponse>(latitude, longitude) {
            daily(OpenMeteoForecastParameters.parameters)
        }.mapResponse("Failed to get forecast weather data from OpenMeteo") {
            it.toForecastList()
        }


    override suspend fun getHourlyWeather(
        latitude: Double,
        longitude: Double
    ): HourlyList =
        request<OpenMeteoHourlyResponse>(latitude, longitude) {
            hourly(OpenMeteoHourlyParameters.parameters)
            forecastDays(2)
        }.mapResponse("Failed to get hourly weather data from OpenMeteo") {
            it.toHourlyList()
        }

    private suspend inline fun <reified T> request(
        lat: Double,
        lon: Double,
        block: OpenMeteoRequestBuilder.() -> Unit
    ): T =
        WeatherClient.json.decodeFromString(
            WeatherClient.client.get(
                OpenMeteoRequestBuilder(url)
                    .lat(lat)
                    .lon(lon)
                    .apply(block)
                    .build()
            ).bodyAsText()
        )

    inline fun <T, R> T.mapResponse(
        error: String,
        mapper: (T) -> R?
    ): R =
        mapper(this) ?: error(error)
}

private class OpenMeteoRequestBuilder(
    private val baseUrl: String
) {

    private var latitude: Double? = null
    private var longitude: Double? = null

    private var current: List<String>? = null
    private var daily: List<String>? = null
    private var hourly: List<String>? = null

    private var forecastDays: Int? = null


    fun lat(value: Double) = apply { latitude = value }

    fun lon(value: Double) = apply { longitude = value }

    fun current(params: List<String>) = apply { current = params }

    fun daily(params: List<String>) = apply { daily = params }

    fun hourly(params: List<String>) = apply { hourly = params }

    fun forecastDays(days: Int) = apply { forecastDays = days }


    fun build(): String =
        URLBuilder(baseUrl).apply {

            path("v1", "forecast")

            parameters.append("latitude", latitude.toString())
            parameters.append("longitude", longitude.toString())

            current?.let { parameters.append("current", it.joinToString(",")) }
            daily?.let { parameters.append("daily", it.joinToString(",")) }
            hourly?.let { parameters.append("hourly", it.joinToString(",")) }

            forecastDays?.let {
                parameters.append("forecast_days", it.toString())
            }

            parameters.append("wind_speed_unit", "ms")

        }.buildString()
}