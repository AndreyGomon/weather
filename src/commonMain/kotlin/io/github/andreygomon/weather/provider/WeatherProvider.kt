package io.github.andreygomon.weather.provider

import io.github.andreygomon.weather.data.ForecastData
import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherData

interface WeatherProvider: WeatherRequests {
    val name: String
    val url: String
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherData
    suspend fun getHourlyWeather(latitude: Double, longitude: Double): HourlyList
    suspend fun getForecastWeather(latitude: Double, longitude: Double): ForecastList
}

interface WeatherRequests {
    val perMinute: Long
    val perHour: Long
    val perDay: Long
    fun getRemaining(limit: Long, used: Long): Long {
        return (limit - used).coerceAtLeast(0)
    }
}