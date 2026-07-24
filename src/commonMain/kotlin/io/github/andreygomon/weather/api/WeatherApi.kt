package io.github.andreygomon.weather.api

import kotlinx.coroutines.flow.StateFlow
import io.github.andreygomon.weather.model.Coordinates
import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.data.WeatherSettings
import io.github.andreygomon.weather.data.WeatherStat
import io.github.andreygomon.weather.data.error.ResultWrapper

interface WeatherApi {
    val stats: StateFlow<List<WeatherStat>>
    val settings: StateFlow<WeatherSettings>
    val currentWeather: StateFlow<WeatherData>
    val currentForecast: StateFlow<ForecastList>
    val currentHourly: StateFlow<HourlyList>

    fun updateSettings(settings: WeatherSettings)

    suspend fun fetchWeather(
        location: Coordinates,
        provider: String = settings.value.provider,
        current: Boolean = false,
    ): ResultWrapper<WeatherData>

    suspend fun fetchForecast(
        location: Coordinates,
        provider: String = settings.value.provider,
        current: Boolean = false,
    ): ResultWrapper<ForecastList>

    suspend fun fetchHourly(
        location: Coordinates,
        provider: String = settings.value.provider,
        current: Boolean = false,
    ): ResultWrapper<HourlyList>
}
