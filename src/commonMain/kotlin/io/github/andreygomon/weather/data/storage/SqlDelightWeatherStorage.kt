package io.github.andreygomon.weather.data.storage

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.SqlDriver
import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.data.WeatherSettings
import io.github.andreygomon.weather.data.WeatherStat
import io.github.andreygomon.weather.model.Coordinates
import io.github.andreygomon.weather.DbCurrentWeather
import io.github.andreygomon.weather.DbForecast
import io.github.andreygomon.weather.DbHourly
import io.github.andreygomon.weather.DbStats
import io.github.andreygomon.weather.DbWeather
import io.github.andreygomon.weather.WeatherDatabase
import io.github.andreygomon.weather.provider.WeatherProviderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class SqlDelightWeatherStorage(
    driver: SqlDriver,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : WeatherStorage {
    private val queries = WeatherDatabase(driver).weatherQueries

    init {
        if (queries.getSettings().executeAsOneOrNull() == null) {
            val defaultProvider = WeatherProviderFactory.getProviderNames().firstOrNull().orEmpty()
            queries.insertSettings(
                provider = defaultProvider,
                source = WeatherProviderFactory.LocationSource.MANUAL.displayName,
            )
        }
    }

    override val stats: StateFlow<List<WeatherStat>> =
        queries.getStats()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map(DbStats::toWeatherStat) }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val settings: StateFlow<WeatherSettings> =
        queries.getSettings()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { row ->
                row?.let { WeatherSettings(it.provider, it.source) }
                    ?: WeatherSettings()
            }
            .stateIn(scope, SharingStarted.Eagerly, WeatherSettings())

    override val currentWeather: StateFlow<WeatherData> =
        queries.getCurrentWeather()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toWeatherData() ?: WeatherData() }
            .stateIn(scope, SharingStarted.Eagerly, WeatherData())

    override val currentForecast: StateFlow<ForecastList> =
        queries.getCurrentForecast()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toForecastList() ?: ForecastList(emptyList()) }
            .stateIn(scope, SharingStarted.Eagerly, ForecastList(emptyList()))

    override val currentHourly: StateFlow<HourlyList> =
        queries.getCurrentHourly()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toHourlyList() ?: HourlyList(emptyList()) }
            .stateIn(scope, SharingStarted.Eagerly, HourlyList(emptyList()))

    override fun getWeather(point: Coordinates, provider: String): WeatherData? =
        queries.getWeather(point.latitude, point.longitude, provider)
            .executeAsOneOrNull()
            ?.toWeatherData()

    override fun getForecast(point: Coordinates, provider: String): ForecastList? =
        queries.getForecast(point.latitude, point.longitude, provider)
            .executeAsOneOrNull()
            ?.toForecastList()

    override fun getHourly(point: Coordinates, provider: String): HourlyList? =
        queries.getHourly(point.latitude, point.longitude, provider)
            .executeAsOneOrNull()
            ?.toHourlyList()

    override fun saveStat(stat: WeatherStat) {
        val current = stat.resetExpired()
        queries.upsertStat(
            provider = current.provider,
            requests = current.requests + 1,
            requestsStart = current.requestsStart,
        )
    }

    override fun saveSettings(settings: WeatherSettings) {
        queries.insertSettings(settings.provider, settings.source)
    }

    override fun saveCurrentWeather(data: WeatherData) {
        queries.insertCurrentWeather(
            timestamp = data.timestamp ?: data.update,
            data_ = WeatherData.toJson(data),
        )
    }

    override fun saveWeather(data: WeatherData) {
        queries.insertWeather(
            latitude = data.latitude,
            longitude = data.longitude,
            timestamp = data.timestamp ?: data.update,
            provider = data.provider,
            data_ = WeatherData.toJson(data),
        )
    }

    override fun saveForecast(
        point: Coordinates,
        provider: String,
        data: ForecastList,
        current: Boolean,
    ) {
        queries.transaction {
            if (current) queries.clearCurrentForecast()
            queries.insertForecast(
                latitude = point.latitude,
                longitude = point.longitude,
                isCurrent = if (current) 1L else 0L,
                timestamp = data.lastUpdate,
                provider = provider,
                data_ = ForecastList.toJson(data),
            )
        }
    }

    override fun saveHourly(
        point: Coordinates,
        provider: String,
        data: HourlyList,
        current: Boolean,
    ) {
        queries.transaction {
            if (current) queries.clearCurrentHourly()
            queries.insertHourly(
                latitude = point.latitude,
                longitude = point.longitude,
                isCurrent = if (current) 1L else 0L,
                timestamp = data.lastUpdate,
                provider = provider,
                data_ = HourlyList.toJson(data),
            )
        }
    }
}

private fun DbStats.toWeatherStat(): WeatherStat = WeatherStat(
    provider = provider,
    requests = requests,
    requestsStart = requestsStart,
)

private fun DbWeather.toWeatherData(): WeatherData = WeatherData.fromJson(data_)
private fun DbCurrentWeather.toWeatherData(): WeatherData = WeatherData.fromJson(data_)
private fun DbForecast.toForecastList(): ForecastList = ForecastList.fromJson(data_)
private fun DbHourly.toHourlyList(): HourlyList = HourlyList.fromJson(data_)
