package io.github.andreygomon.weather.data.storage

import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.data.WeatherSettings
import io.github.andreygomon.weather.data.WeatherStat
import io.github.andreygomon.weather.model.Coordinates
import kotlinx.coroutines.flow.StateFlow

internal interface WeatherStorage {
    val stats: StateFlow<List<WeatherStat>>
    val settings: StateFlow<WeatherSettings>
    val currentWeather: StateFlow<WeatherData>
    val currentForecast: StateFlow<ForecastList>
    val currentHourly: StateFlow<HourlyList>

    fun getWeather(point: Coordinates, provider: String): WeatherData?
    fun getForecast(point: Coordinates, provider: String): ForecastList?
    fun getHourly(point: Coordinates, provider: String): HourlyList?

    fun saveStat(stat: WeatherStat)
    fun saveSettings(settings: WeatherSettings)
    fun saveCurrentWeather(data: WeatherData)
    fun saveWeather(data: WeatherData)
    fun saveForecast(point: Coordinates, provider: String, data: ForecastList, current: Boolean)
    fun saveHourly(point: Coordinates, provider: String, data: HourlyList, current: Boolean)
}
