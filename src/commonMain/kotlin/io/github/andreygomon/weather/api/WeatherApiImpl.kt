package io.github.andreygomon.weather.api

import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.data.WeatherSettings
import io.github.andreygomon.weather.data.WeatherSource
import io.github.andreygomon.weather.data.WeatherStat
import io.github.andreygomon.weather.data.error.ErrorType
import io.github.andreygomon.weather.data.error.ResultWrapper
import io.github.andreygomon.weather.data.error.safeCall
import io.github.andreygomon.weather.data.storage.WeatherStorage
import io.github.andreygomon.weather.model.Coordinates
import io.github.andreygomon.weather.provider.LocationProvider
import io.github.andreygomon.weather.provider.LocationProviderFactory
import io.github.andreygomon.weather.provider.WeatherProvider
import io.github.andreygomon.weather.provider.WeatherProviderFactory
import io.github.andreygomon.weather.provider.nominatim.NominatimProvider
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

internal class WeatherApiImpl(
    private val storage: WeatherStorage,
    private val locationProvider: LocationProvider? =
        LocationProviderFactory.createProvider(NominatimProvider.NAME),
) : WeatherApi {

    companion object {
        val CURRENT_TTL: Duration = 15.minutes
        val FORECAST_TTL: Duration = 60.minutes
        val HOURLY_TTL: Duration = 60.minutes
    }

    override val stats: StateFlow<List<WeatherStat>> = storage.stats
    override val settings: StateFlow<WeatherSettings> = storage.settings
    override val currentWeather: StateFlow<WeatherData> = storage.currentWeather
    override val currentForecast: StateFlow<ForecastList> = storage.currentForecast
    override val currentHourly: StateFlow<HourlyList> = storage.currentHourly

    override fun updateSettings(settings: WeatherSettings) {
        storage.saveSettings(settings)
    }

    override suspend fun fetchWeather(
        location: Coordinates,
        provider: String,
        current: Boolean,
    ): ResultWrapper<WeatherData> = fetch(
        ttl = CURRENT_TTL,
        providerName = provider,
        cached = { storage.getWeather(location, provider) },
        request = { weatherProvider ->
            val locality = fetchLocation(location)
            weatherProvider
                .getWeather(location.latitude, location.longitude)
                .copy(locality = locality)
        },
        save = { data, _, saveAsCurrent ->
            if (saveAsCurrent) storage.saveCurrentWeather(data)
            storage.saveWeather(data)
        },
        current = current,
    )

    override suspend fun fetchForecast(
        location: Coordinates,
        provider: String,
        current: Boolean,
    ): ResultWrapper<ForecastList> = fetch(
        ttl = FORECAST_TTL,
        providerName = provider,
        cached = { storage.getForecast(location, provider) },
        request = { weatherProvider ->
            weatherProvider.getForecastWeather(location.latitude, location.longitude)
        },
        save = { data, weatherProvider, saveAsCurrent ->
            storage.saveForecast(
                point = location,
                provider = weatherProvider.name,
                data = data,
                current = saveAsCurrent,
            )
        },
        current = current,
    )

    override suspend fun fetchHourly(
        location: Coordinates,
        provider: String,
        current: Boolean,
    ): ResultWrapper<HourlyList> = fetch(
        ttl = HOURLY_TTL,
        providerName = provider,
        cached = { storage.getHourly(location, provider) },
        request = { weatherProvider ->
            weatherProvider.getHourlyWeather(location.latitude, location.longitude)
        },
        save = { data, weatherProvider, saveAsCurrent ->
            storage.saveHourly(
                point = location,
                provider = weatherProvider.name,
                data = data,
                current = saveAsCurrent,
            )
        },
        current = current,
    )

    private suspend fun <T : WeatherSource> fetch(
        ttl: Duration,
        providerName: String,
        cached: () -> T?,
        request: suspend (WeatherProvider) -> T,
        save: (T, WeatherProvider, Boolean) -> Unit,
        current: Boolean,
    ): ResultWrapper<T> {
        val provider = WeatherProviderFactory.getProvider(providerName)
            ?: return ResultWrapper.Failure(
                ErrorType.Unexpected(
                    IllegalStateException("Не выбран источник погодных данных"),
                ),
            )

        val cachedData = cached()
        if (cachedData != null && isActual(cachedData, ttl)) {
            if (current) save(cachedData, provider, true)
            return ResultWrapper.Success(cachedData)
        }

        return when (val result = safeCall { request(provider) }) {
            is ResultWrapper.Success -> {
                save(result.value, provider, current)
                updateStats(provider)
                result
            }

            is ResultWrapper.Failure -> result
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun isActual(
        data: WeatherSource,
        ttl: Duration,
        now: Long = Clock.System.now().toEpochMilliseconds(),
    ): Boolean = now - data.update < ttl.inWholeMilliseconds

    @OptIn(ExperimentalTime::class)
    private fun updateStats(provider: WeatherProvider) {
        val now = Clock.System.now().toEpochMilliseconds()
        val stat = stats.value.find { it.provider == provider.name }
            ?: WeatherStat(
                provider = provider.name,
                requests = 0,
                requestsStart = now,
            )

        storage.saveStat(stat)
    }

    private suspend fun fetchLocation(location: Coordinates): String {
        val provider = locationProvider ?: return "Неизвестно"
        return when (
            val result = safeCall {
                provider.getLocation(location.latitude, location.longitude)
            }
        ) {
            is ResultWrapper.Success -> result.value
            is ResultWrapper.Failure -> "Не удалось определить местоположение"
        }
    }
}
