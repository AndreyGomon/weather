package ru.mascot.features.weather.api

import kotlinx.coroutines.flow.StateFlow
import org.oscim.core.GeoPoint
import ru.mascot.features.weather.data.ForecastList
import ru.mascot.features.weather.data.HourlyList
import ru.mascot.features.weather.data.WeatherData
import ru.mascot.features.weather.data.WeatherSettings
import ru.mascot.features.weather.data.WeatherStat
import ru.mascot.features.weather.data.error.ResultWrapper

interface WeatherApi {
    val stats: StateFlow<List<WeatherStat>>
    val settings: StateFlow<WeatherSettings>
    val currentWeather: StateFlow<WeatherData>
    val currentForecast: StateFlow<ForecastList>
    val currentHourly: StateFlow<HourlyList>

    fun updateSettings(settings: WeatherSettings)

    suspend fun fetchWeather(
        location: GeoPoint,
        provider: String = settings.value.provider,
        current: Boolean
    ): ResultWrapper<WeatherData>

    suspend fun fetchForecast(
        location: GeoPoint,
        provider: String = settings.value.provider,
        current: Boolean
    ): ResultWrapper<ForecastList>

    suspend fun fetchHourly(
        location: GeoPoint,
        provider: String = settings.value.provider,
        current: Boolean
    ): ResultWrapper<HourlyList>
}
