package ru.mascot.features.weather.api

import kotlin.time.Clock
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Single
import org.oscim.core.GeoPoint
import ru.mascot.features.weather.data.ForecastList
import ru.mascot.features.weather.data.HourlyList
import ru.mascot.features.weather.data.WeatherData
import ru.mascot.features.weather.data.WeatherRepository
import ru.mascot.features.weather.data.WeatherSettings
import ru.mascot.features.weather.data.WeatherSource
import ru.mascot.features.weather.data.WeatherStat
import ru.mascot.features.weather.data.error.ErrorType
import ru.mascot.features.weather.data.error.ResultWrapper
import ru.mascot.features.weather.data.error.safeCall
import ru.mascot.features.weather.provider.LocationProviderFactory
import ru.mascot.features.weather.provider.WeatherProvider
import ru.mascot.features.weather.provider.WeatherProviderFactory
import ru.mascot.features.weather.provider.nomenatim.NominatimProvider
import java.lang.IllegalStateException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Single(binds = [WeatherApi::class])
internal class WeatherApiImpl : WeatherApi {
    companion object {
        val CURRENT_TTL: Duration = 15.minutes
        val FORECAST_TTL: Duration = 60.minutes
        val HOURLY_TTL: Duration = 60.minutes
    }

    private var locationProvider = LocationProviderFactory.createProvider(NominatimProvider().name)

    override val stats: StateFlow<List<WeatherStat>> = WeatherRepository.stats
    override val settings: StateFlow<WeatherSettings> = WeatherRepository.settings

    override val currentWeather: StateFlow<WeatherData> = WeatherRepository.currentWeather
    override val currentForecast: StateFlow<ForecastList> = WeatherRepository.currentForecast
    override val currentHourly: StateFlow<HourlyList> = WeatherRepository.currentHourly

    override fun updateSettings(settings: WeatherSettings) {
        WeatherRepository.addSettings(settings)
    }

    private suspend fun <T: WeatherSource> fetch(
        ttl: Duration,
        provider: String,
        cached: () -> T?,
        request: suspend (WeatherProvider) -> T,
        save: (T, WeatherProvider, Boolean) -> Unit,
        current: Boolean
    ): ResultWrapper<T> {
        isActual(ttl, get = cached).let { (actual, data) ->
            if (actual && data != null) {
                return ResultWrapper.Success(data)
            }
        }

        val provider = WeatherProviderFactory.getProvider(provider)
            ?: return ResultWrapper.Failure(
                ErrorType.Unexpected(IllegalStateException("Выберете источник погодных данных"))
            )

        return when (val result = safeCall { request(provider) }) {
            is ResultWrapper.Success -> {
                save(result.value, provider, current)
                updateStats(provider)
                ResultWrapper.Success(result.value)
            }
            is ResultWrapper.Failure -> result
        }
    }

    override suspend fun fetchWeather(location: GeoPoint, provider: String, current: Boolean): ResultWrapper<WeatherData> =
        fetch(
            CURRENT_TTL,
            provider,
            cached = { WeatherRepository.getWeather(location)},
            request = { provider ->
                val locality = fetchLocation(location)
                provider.getWeather(location.latitude, location.longitude).copy(locality = locality)
            },
            save = { data, _, current ->
                if (current) WeatherRepository.addCurrentWeather(data) else  WeatherRepository.addWeather(data)
            },
            current = current
        )

    override suspend fun fetchForecast(location: GeoPoint, provider: String, current: Boolean): ResultWrapper<ForecastList> =
        fetch(
            FORECAST_TTL,
            provider,
            cached = { WeatherRepository.getForecast(location)},
            request = { provider ->
                provider.getForecastWeather(location.latitude, location.longitude)
            },
            save = { data, provider, current ->
                WeatherRepository.addForecast(
                    point = location,
                    provider = provider.name,
                    data = data,
                    current = current
                )
            },
            current = current
        )

    override suspend fun fetchHourly(location: GeoPoint, provider: String, current: Boolean): ResultWrapper<HourlyList> =
        fetch(
            HOURLY_TTL,
            provider,
            cached = { WeatherRepository.getHourly(location)},
            request = { provider ->
                provider.getHourlyWeather(location.latitude, location.longitude)
            },
            save = { data, provider, current ->
                WeatherRepository.addHourly(
                    point = location,
                    provider = provider.name,
                    data = data,
                    current = current
                )
            },
            current = current
        )

    private fun <T : WeatherSource> isActual(
        ttl: Duration,
        now: Long = Clock.System.now().toEpochMilliseconds(),
        get: () -> T?
    ): Pair<Boolean, T?> {
        val data = get() ?: return false to null
        val last = data.update
        return ((now - last) < ttl.inWholeMilliseconds) to data
    }

    private fun updateStats(provider: WeatherProvider) {
        val stat = stats.value.find { it.provider == provider.name }
            ?: WeatherStat(
                provider = provider.name,
                requests = 0,
                requestsStart = Clock.System.now().toEpochMilliseconds()
            )

        WeatherRepository.addStat(stat)
    }

    private suspend fun fetchLocation(location: GeoPoint): String {
        val p = locationProvider ?: return "Ошибка создания провайдера"
        return when (val result = safeCall {
            p.getLocation(location.latitude, location.longitude)
        }) {
            is ResultWrapper.Success -> result.value
            is ResultWrapper.Failure -> "Ошибка запроса локации"
        }
    }
}
