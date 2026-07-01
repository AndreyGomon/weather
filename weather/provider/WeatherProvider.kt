package ru.mascot.features.weather.provider

import ru.mascot.features.weather.data.ForecastData
import ru.mascot.features.weather.data.ForecastList
import ru.mascot.features.weather.data.HourlyList
import ru.mascot.features.weather.data.WeatherData

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