package io.github.andreygomon.weather.data

import kotlin.time.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import io.github.andreygomon.weather.model.Coordinates
import io.github.andreygomon.weather.util.DateDay
import kotlin.time.ExperimentalTime

/**
 * Represents a user profile for serialization.
 *
 * @property timestamp Временная метка
 * @property temperature Значение температуры в Цельсиях
 * @property felt Значение температуры ощущения в Цельсиях
 * @property humidity Влажность
 * @property isDay День/Ночь
 * @property precipitation Осадки в мм
 * @property code Код погоды
 * @property cloud Облачность в процентах
 * @property seaPressure Давление на уровне моря
 * @property pressure Давление
 * @property windSpeed Скорость ветра
 * @property windDirection Направление ветра
 */
@Serializable
data class WeatherData(
    @SerialName("latitude") val latitude: Double = 0.0,
    @SerialName("longitude") val longitude: Double = 0.0,
    @SerialName("timestamp") val timestamp: Long? = null,

    @SerialName("temperature") val temperature: Double? = null,
    @SerialName("temperature80m") val temperature80m: Double? = null,
    @SerialName("temperature120m") val temperature120m: Double? = null,
    @SerialName("temperature180m") val temperature180m: Double? = null,

    @SerialName("felt") val felt: Double? = null,

    @SerialName("humidity") val humidity: Int? = null,

    @SerialName("isDay") val isDay: Int? = null,

    @SerialName("precipitation") val precipitation: Double? = null,
    @SerialName("precipitationProbability") val precipitationProbability: Int? = null,

    @SerialName("code") val code: WeatherCode = WeatherCode.Unknown,
    @SerialName("cloud") val cloud: Int? = null,
    @SerialName("cloudLow") val cloudLow: Int? = null,
    @SerialName("cloudMid") val cloudMid: Int? = null,
    @SerialName("cloudHigh") val cloudHigh: Int? = null,

    @SerialName("seaPressure") val seaPressure: Double? = null,
    @SerialName("pressure") val pressure: Double? = null,

    @SerialName("windSpeed") val windSpeed: Double? = null,
    @SerialName("windSpeed80m") val windSpeed80m: Double? = null,
    @SerialName("windSpeed120m") val windSpeed120m: Double? = null,
    @SerialName("windSpeed180m") val windSpeed180m: Double? = null,
    @SerialName("windDirection") val windDirection: Int? = null,
    @SerialName("windDirection80m") val windDirection80m: Int? = null,
    @SerialName("windDirection120m") val windDirection120m: Int? = null,
    @SerialName("windDirection180m") val windDirection180m: Int? = null,
    @SerialName("windGusts10m") val windGusts10m: Double? = null,

    @SerialName("visibility") val visibility: Int? = null,

    @SerialName("snowfall") val snowPrecipitation: Double? = null,
    @SerialName("snow_depth") val snowCover: Double? = null,
    @SerialName("rain") val rainPrecipitation: Double? = null,
    @SerialName("showers") val showerPrecipitation: Double? = null,

    @SerialName("shortwaveRadiation") val shortwaveRadiation: Double? = null,
    @SerialName("directRadiation") val directRadiation: Double? = null,
    @SerialName("directNormalIrradiance") val directNormalIrradiance: Double? = null,
    @SerialName("globalTiltedIrradiance") val globalTiltedIrradiance: Double? = null,
    @SerialName("globalTiltedIrradianceInstant") val globalTiltedIrradianceInstant: Double? = null,
    @SerialName("diffuseRadiation") val diffuseRadiation: Double? = null,
    @SerialName("sunshineDuration") val sunshineDuration: Double? = null,

    @SerialName("soilTemperature0cm") val soilTemperature0cm: Double? = null,
    @SerialName("soilTemperature6cm") val soilTemperature6cm: Double? = null,
    @SerialName("soilTemperature18cm") val soilTemperature18cm: Double? = null,
    @SerialName("soilTemperature54cm") val soilTemperature54cm: Double? = null,

    @SerialName("soilMoisture0to1cm") val soilMoisture0to1cm: Double? = null,
    @SerialName("soilMoisture1to3cm") val soilMoisture1to3cm: Double? = null,
    @SerialName("soilMoisture3to9cm") val soilMoisture3to9cm: Double? = null,
    @SerialName("soilMoisture9to27cm") val soilMoisture9to27cm: Double? = null,
    @SerialName("soilMoisture27to81cm") val soilMoisture27to81cm: Double? = null,

    @SerialName("dewPoint2m") val dewPoint2m: Double? = null,
    @SerialName("lightningPotential") val lightningPotential: Double? = null,
    @SerialName("freezingLevelHeight") val freezingLevelHeight: Double? = null,
    @SerialName("cape") val cape: Double? = null,
    @SerialName("vapourPressureDeficit") val vapourPressureDeficit: Double? = null,
    @SerialName("evapotranspiration") val evapotranspiration: Double? = null,
    @SerialName("et0FaoEvapotranspiration") val et0FaoEvapotranspiration: Double? = null,

    @SerialName("locality") val locality: String = "Неизвестно",
    @SerialName("provider") val provider: String = "Неизвестно",
    @SerialName("lastUpdate") val lastUpdate: Long = 0L,
    @SerialName("description") val description: String = "Default",

    override val name: String = "Default",
    override val update: Long = lastUpdate
) : WeatherSource {
    companion object {
        private val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        fun fromJson(jsonString: String): WeatherData = json.decodeFromString(jsonString)
        fun toJson(data: WeatherData): String = json.encodeToString(data)
    }

    // ── API совместимые helpers ──────────────────────────────────────────────────
    fun getPoint(): Coordinates = Coordinates(latitude, longitude)

    fun isDay(): Boolean = (isDay ?: 0) != 0
    fun windDirection10() = windDirection?.let { WindDirection.from(it) } ?: WindDirection.UNKNOWN
    fun windDirection80() = windDirection80m?.let { WindDirection.from(it) } ?: WindDirection.UNKNOWN
    fun windDirection120() = windDirection120m?.let { WindDirection.from(it) } ?: WindDirection.UNKNOWN
    fun windDirection180() = windDirection180m?.let { WindDirection.from(it) } ?: WindDirection.UNKNOWN

    // ── Форматированные “get”-свойства (в стиле HourlyData) ─────────────────────
    val getTemperature: String get() = temperature?.let { "${it.toInt()}°" } ?: "?"
    fun getDayWeek(short: Boolean): String = timestamp?.let { DateDay.formatDayWeek(it, short = short) } ?: "?"
    fun getDateMonth(short: Boolean): String = timestamp?.let { DateDay.formatDateMonth(it, short = short) } ?: "?"
    val getHour: String get() = timestamp?.let { DateDay.formatHour(it) } ?: "?"
    val getTemperature80: String get() = temperature80m?.let { "${it.toInt()}°" } ?: "?"
    val getTemperature120: String get() = temperature120m?.let { "${it.toInt()}°" } ?: "?"
    val getTemperature180: String get() = temperature180m?.let { "${it.toInt()}°" } ?: "?"
    val getFelt: String get() = felt?.let { "${it.toInt()}°" } ?: "?"
    val getDewPoint: String get() = dewPoint2m?.let { "${it.toInt()}°" } ?: "?"

    val getHumidity: String get() = humidity?.let { "$it%" } ?: "?"
    val getPrecipitation: String get() = precipitation?.let { "$it мм" } ?: "?"
    val getPrecipitationProbability: String get() = precipitationProbability?.let { "$it%" } ?: "?"

    val getCloud: String get() = cloud?.let { "$it%" } ?: "?"
    val getCloudLow: String get() = cloudLow?.let { "$it%" } ?: "?"
    val getCloudMid: String get() = cloudMid?.let { "$it%" } ?: "?"
    val getCloudHigh: String get() = cloudHigh?.let { "$it%" } ?: "?"

    val getSeaPressure: String get() = seaPressure?.let { "${it.toInt()} мм рт. ст." } ?: "?"
    val getPressure: String get() = pressure?.let { "${it.toInt()} мм рт. ст." } ?: "?"

    val getWindSpeed: String get() = windSpeed?.let { "$it м/с" } ?: "?"
    val getWindSpeed80: String get() = windSpeed80m?.let { "$it м/с" } ?: "?"
    val getWindSpeed120: String get() = windSpeed120m?.let { "$it м/с" } ?: "?"
    val getWindSpeed180: String get() = windSpeed180m?.let { "$it м/с" } ?: "?"
    val getWindGusts10: String get() = windGusts10m?.let { "$it м/с" } ?: "?"

    val getSoilMoisture0to1cm: String get() = soilMoisture0to1cm?.let { "${it*100}%" } ?: "?"
    val getSoilMoisture1to3cm: String get() = soilMoisture1to3cm?.let { "${it*100}%" } ?: "?"
    val getSoilMoisture3to9cm: String get() = soilMoisture3to9cm?.let { "${it*100}%" } ?: "?"
    val getSoilMoisture9to27cm: String get() = soilMoisture9to27cm?.let { "${it*100}%" } ?: "?"
    val getSoilMoisture27to81cm: String get() = soilMoisture27to81cm?.let { "${it*100}%" } ?: "?"

    val getVisibility: String
        get() = visibility?.let { m ->
            if (m >= 1000) "${(m / 1000.0)} км" else "$m м"
        } ?: "?"


}

@Serializable
data class ForecastList(
    @SerialName("list") val list: List<ForecastData>,
    @SerialName("lastUpdate") val lastUpdate: Long = 0L,
    override val name: String = "Default",
    override val update: Long = lastUpdate
    ) : WeatherSource {
    companion object {
        private val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        fun fromJson(jsonString: String): ForecastList {
            return json.decodeFromString(jsonString)
        }

        fun toJson(data: ForecastList): String {
            return json.encodeToString(data)
        }
    }
}

@Serializable
data class ForecastData(
    @SerialName("timestamp") val timestamp: Long? = null,
    @SerialName("code") val code: WeatherCode = WeatherCode.Unknown,
    @SerialName("temperatureMax") val temperatureMax: Double? = null,
    @SerialName("temperatureMin") val temperatureMin: Double? = null,
    @SerialName("feltMax") val feltMax: Double? = null,
    @SerialName("feltMin") val feltMin: Double? = null,
    @SerialName("precipitation") val precipitation: Double? = null,
    @SerialName("sunrise") val sunrise: Long? = null,
    @SerialName("sunset") val sunset: Long? = null,
    @SerialName("windSpeed") val windSpeed: Double? = null,
    @SerialName("windDirection") val windDirection: Int? = null
) {
    fun windDirection() = windDirection?.let { WindDirection.from(it) }
    fun getDayWeek(short: Boolean) : String = timestamp?.let { DateDay.formatDayWeek(it, short = short) } ?: "?"
    fun getDateMonth(short: Boolean): String = timestamp?.let { DateDay.formatDateMonth(it, short = short) } ?: "?"
    val getTemperatureMax: String get() = temperatureMax?.let { "${it.toInt()}°" } ?: "?"
    val getTemperatureMin: String get() = temperatureMin?.let { "${it.toInt()}°" } ?: "?"
    val getFeltMax: String get() = feltMax?.let { "${it.toInt()}°" } ?: "?"
    val getFeltMin: String get() = feltMin?.let { "${it.toInt()}°" } ?: "?"
    val getPrecipitation: String get() = precipitation?.toInt()?.let { "$it%" } ?: "?"
    val getWindSpeed: String get() = windSpeed?.let { "$it м/с" } ?: "?"
}

@Serializable
data class HourlyList(
    @SerialName("list") val list: List<HourlyData>,
    @SerialName("lastUpdate") val lastUpdate: Long = 0L,
    override val name: String = "Hourly",
    override val update: Long = lastUpdate
) : WeatherSource {
    private val HOUR_MS = 3_600_000L

    data class Last24Hours(
        val hours: List<HourlyData>,
        val isStale: Boolean
    )

    companion object {
        private val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        fun fromJson(jsonString: String): HourlyList {
            return json.decodeFromString(jsonString)
        }

        fun toJson(data: HourlyList): String {
            return json.encodeToString(data)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun next24Hours(): Last24Hours {
        val now = Clock.System.now().toEpochMilliseconds()
        val end = now + 24 * HOUR_MS

        val filtered = list.filter { data ->
            val timestamp = data.timestamp ?: return@filter false
            timestamp in now..end
        }

        val isStale = filtered.size < 24

        return Last24Hours(
            hours = if (filtered.size >= 24) filtered else list.takeLast(24),
            isStale = isStale
        )
    }
}

@Serializable
data class HourlyData(
    @SerialName("timestamp") val timestamp: Long? = null,
    @SerialName("code") val code: WeatherCode = WeatherCode.Unknown,
    @SerialName("temperature") val temperature: Double? = null,
    @SerialName("temperature80m") val temperature80m: Double? = null,
    @SerialName("temperature120m") val temperature120m: Double? = null,
    @SerialName("temperature180m") val temperature180m: Double? = null,
    @SerialName("apparentTemperature") val apparentTemperature: Double? = null,
    @SerialName("dewPoint") val dewPoint: Double? = null,
    @SerialName("humidity") val humidity: Int? = null,
    @SerialName("precipitationProbability") val precipitationProbability: Int? = null,
    @SerialName("windSpeed10m") val windSpeed10m: Double? = null,
    @SerialName("windSpeed80m") val windSpeed80m: Double? = null,
    @SerialName("windSpeed120m") val windSpeed120m: Double? = null,
    @SerialName("windSpeed180m") val windSpeed180m: Double? = null,
    @SerialName("windDirection10m") val windDirection10m: Int? = null,
    @SerialName("windDirection80m") val windDirection80m: Int? = null,
    @SerialName("windDirection120m") val windDirection120m: Int? = null,
    @SerialName("windDirection180m") val windDirection180m: Int? = null
) {
    fun windDirection10() = windDirection10m?.let { WindDirection.from(it) }
    fun windDirection80() = windDirection80m?.let { WindDirection.from(it) }
    fun windDirection120() = windDirection120m?.let { WindDirection.from(it) }
    fun windDirection180() = windDirection180m?.let { WindDirection.from(it) }

    val getHour: String get() = timestamp?.let { DateDay.formatHour(it) } ?: "?"
    val getTemperature: String get() = temperature?.let { "${it.toInt()}°" } ?: "?"
    val getTemperature80: String get() = temperature80m?.let { "${it.toInt()}°" } ?: "?"
    val getTemperature120: String get() = temperature120m?.let { "${it.toInt()}°" } ?: "?"
    val getTemperature180: String get() = temperature180m?.let { "${it.toInt()}°" } ?: "?"
    val getApparentTemperature: String get() = apparentTemperature?.let { "${it.toInt()}°" } ?: "?"
    val getDewPoint: String get() = dewPoint?.let { "${it.toInt()}°" } ?: "?"
    val getHumidity: String get() = humidity?.let { "$it%" } ?: "?"
    val getPrecipitationProbability: String get() = precipitationProbability?.let { "$it%" } ?: "?"
    val getWindSpeed10: String get() = windSpeed10m?.let { "$it м/с" } ?: "?"
    val getWindSpeed80: String get() = windSpeed80m?.let { "$it м/с" } ?: "?"
    val getWindSpeed120: String get() = windSpeed120m?.let { "$it м/с" } ?: "?"
    val getWindSpeed180: String get() = windSpeed180m?.let { "$it м/с" } ?: "?"
}

data class WeatherSettings(
    val provider: String = "",
    val source: String = ""
)

data class WeatherStat(
    val provider : String = "",
    val requests: Long = 0,
    val requestsStart: Long = 0
) {
    @OptIn(ExperimentalTime::class)
    fun getExpired(current: Long = Clock.System.now().toEpochMilliseconds()): Boolean {
        return current - requestsStart >= 86_400_000L
    }

    @OptIn(ExperimentalTime::class)
    fun resetExpired(current: Long = Clock.System.now().toEpochMilliseconds()): WeatherStat {
        return if (getExpired(current)) {
            copy(requests = 0, requestsStart = current)
        } else {
            this
        }
    }
}
