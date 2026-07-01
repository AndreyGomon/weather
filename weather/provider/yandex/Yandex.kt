package ru.mascot.features.weather.provider.yandex

import ru.mascot.features.weather.data.ForecastData
import ru.mascot.features.weather.data.ForecastList
import ru.mascot.features.weather.data.HourlyList
import ru.mascot.features.weather.data.WeatherData
import ru.mascot.features.weather.provider.WeatherProvider
import ru.mascot.features.weather.provider.openMeteo.OpenMeteoRequests

class Yandex : WeatherProvider {
    companion object {
        const val NAME = "Yandex"
    }

    override val name = NAME
    override val url = "https://ya.ru"

    override val perMinute: Long
        get() = 10
    override val perHour: Long
        get() = 600
    override val perDay: Long
        get() = 2400

    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherData {
        TODO("Not yet implemented")
    }

    override suspend fun getHourlyWeather(
        latitude: Double,
        longitude: Double
    ): HourlyList {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastWeather(latitude: Double, longitude: Double): ForecastList {
        TODO("Not yet implemented")
    }
}