package ru.mascot.features.weather.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.oscim.core.GeoPoint
import ru.mascot.features.DbCurrentWeather
import ru.mascot.features.DbForecast
import ru.mascot.features.DbHourly
import ru.mascot.features.DbStats
import ru.mascot.features.DbWeather
import ru.mascot.features.database.DatabaseHolder
import ru.mascot.features.weather.provider.WeatherProviderFactory

internal object WeatherRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    val stats: StateFlow<List<WeatherStat>> =
        DatabaseHolder.db.weatherQueries.getStats().asFlow().mapToList(Dispatchers.IO)
            .map { list ->
                list.map { stats ->
                    stats.toWeatherStats()
                }
            }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    val settings: StateFlow<WeatherSettings> =
        DatabaseHolder.db.weatherQueries.getSettings().asFlow().mapToOneOrNull(Dispatchers.IO)
            .map  { dbSettings ->
                dbSettings?.let {
                    WeatherSettings(it.provider, it.source)
                } ?: run {
                    val default = WeatherSettings(
                        provider = WeatherProviderFactory.getProviderNames().first(),
                        source = WeatherProviderFactory.LocationSource.CENTER.displayName
                    )
                    DatabaseHolder.db.weatherQueries.insertSettings(
                        default.provider,
                        default.source
                    )
                    default
                }
            }.stateIn(scope, SharingStarted.Eagerly, WeatherSettings())

    val currentWeather: StateFlow<WeatherData> =
        DatabaseHolder.db.weatherQueries.getCurrentWeather().asFlow().mapToOneOrNull(Dispatchers.IO)
            .map {
                it?.toWeatherData() ?: WeatherData()
            }.stateIn(scope, SharingStarted.Eagerly, WeatherData())

    val currentForecast: StateFlow<ForecastList> =
        DatabaseHolder.db.weatherQueries.getCurrentForecast().asFlow().mapToOneOrNull(Dispatchers.IO)
            .map {
                it?.toWeatherForecast() ?: ForecastList(listOf())
            }.stateIn(scope, SharingStarted.Eagerly, ForecastList(listOf()))

    val currentHourly: StateFlow<HourlyList> =
        DatabaseHolder.db.weatherQueries.getCurrentHourly().asFlow().mapToOneOrNull(Dispatchers.IO)
            .map {
                it?.toWeatherHourly() ?: HourlyList(listOf())
            }.stateIn(scope, SharingStarted.Eagerly, HourlyList(listOf()))

    fun getWeather(point: GeoPoint) : WeatherData? = DatabaseHolder.db.weatherQueries.getWeather(point).executeAsOneOrNull()?.toWeatherData()
    fun getForecast(point: GeoPoint) : ForecastList? = DatabaseHolder.db.weatherQueries.getForecast(point).executeAsOneOrNull()?.toWeatherForecast()
    fun getHourly(point: GeoPoint) : HourlyList? = DatabaseHolder.db.weatherQueries.getHourly(point).executeAsOneOrNull()?.toWeatherHourly()

    fun addStat(stat: WeatherStat) {
        val updated = stat.resetExpired()
        DatabaseHolder.db.weatherQueries.transaction {
            DatabaseHolder.db.weatherQueries.insertStat(updated.provider, updated.requests, updated.requestsStart)
            DatabaseHolder.db.weatherQueries.updateStat(updated.provider)
        }
    }

    fun addSettings(settings: WeatherSettings) {
        DatabaseHolder.db.weatherQueries.insertSettings(settings.provider, settings.source)
    }

    //TODO Исправить в базе или решить проблему null таймштампа
    fun addCurrentWeather(data: WeatherData) {
        DatabaseHolder.db.weatherQueries.insertCurrentWeather(
            data.timestamp ?: 0,
            WeatherData.toJson(data)
        )
    }

    fun addWeather(data: WeatherData) {
        DatabaseHolder.db.weatherQueries.insertWeather(
            data.getPoint(),
            data.timestamp ?: 0,
            data.provider,
            WeatherData.toJson(data)
        )
    }

    fun addForecast(point: GeoPoint, provider: String, data: ForecastList, current: Boolean) {
        DatabaseHolder.db.weatherQueries.insertForecast(
            point = point,
            currentId = if (current) DatabaseHolder.db.weatherQueries.getCurrentWeatherId().executeAsOne() else null,
            timestamp = data.lastUpdate,
            provider = provider,
            data_ = ForecastList.toJson(data)
        )
    }

    fun addHourly(point: GeoPoint, provider: String, data: HourlyList, current: Boolean) {
        DatabaseHolder.db.weatherQueries.insertHourly(
            point = point,
            currentId = if (current) DatabaseHolder.db.weatherQueries.getCurrentWeatherId().executeAsOne() else null,
            timestamp = data.lastUpdate,
            provider = provider,
            data_ = HourlyList.toJson(data)
        )
    }
}

fun DbStats.toWeatherStats(): WeatherStat {
    return WeatherStat(
        provider = this.provider,
        requests = this.requests,
        requestsStart = this.requestsStart
    )
}

fun DbWeather.toWeatherData(): WeatherData {
    return WeatherData.fromJson(this.data_)
}

fun DbCurrentWeather.toWeatherData(): WeatherData {
    return WeatherData.fromJson(this.data_)
}

fun DbForecast.toWeatherForecast(): ForecastList {
    return ForecastList.fromJson(this.data_)
}

fun DbHourly.toWeatherHourly(): HourlyList {
    return HourlyList.fromJson(this.data_)
}
