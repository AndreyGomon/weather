package io.github.andreygomon.weather.service

import io.github.andreygomon.weather.api.WeatherApi
import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.data.WeatherSettings
import io.github.andreygomon.weather.data.WeatherStat
import io.github.andreygomon.weather.model.Coordinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * Optional polling service. The host application supplies the current coordinates,
 * so this library stays independent from any map or geolocation framework.
 */
class WeatherService(
    private val weatherApi: WeatherApi,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) {
    val stats: StateFlow<List<WeatherStat>> get() = weatherApi.stats
    val settings: StateFlow<WeatherSettings> get() = weatherApi.settings
    val currentWeather: StateFlow<WeatherData> get() = weatherApi.currentWeather
    val currentForecast: StateFlow<ForecastList> get() = weatherApi.currentForecast
    val currentHourly: StateFlow<HourlyList> get() = weatherApi.currentHourly

    private var pollingJob: Job? = null

    fun start(
        interval: Duration = 15.minutes,
        coordinates: suspend () -> Coordinates?,
        includeForecast: Boolean = false,
        includeHourly: Boolean = false,
    ) {
        stop()
        pollingJob = scope.launch {
            while (isActive) {
                coordinates()?.let { point ->
                    val provider = weatherApi.settings.value.provider
                    weatherApi.fetchWeather(point, provider, current = true)
                    if (includeForecast) {
                        weatherApi.fetchForecast(point, provider, current = true)
                    }
                    if (includeHourly) {
                        weatherApi.fetchHourly(point, provider, current = true)
                    }
                }
                delay(interval)
            }
        }
    }

    fun stop() {
        pollingJob?.cancel()
        pollingJob = null
    }
}
