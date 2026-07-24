package io.github.andreygomon.weather.provider.openmeteo

import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import io.github.andreygomon.weather.data.ForecastData
import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.HourlyData
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherCode
import io.github.andreygomon.weather.data.WeatherData
import kotlin.time.ExperimentalTime

enum class OpenMeteoRequests(val maxRequests: Long) {
    PER_MINUTE(600),
    PER_HOUR(5000),
    PER_DAY(10000);

    companion object {
        fun fromString(period: String): OpenMeteoRequests {
            return when (period.lowercase()) {
                "minute" -> PER_MINUTE
                "hour" -> PER_HOUR
                "day" -> PER_DAY
                else -> throw IllegalArgumentException("Unknown period: $period")
            }
        }
    }
}

object OpenMeteoCurrentParameters {
    val parameters: List<String> = listOf(
        "weather_code",
        "temperature_2m",
        "temperature_80m",
        "temperature_120m",
        "temperature_180m",
        "relative_humidity_2m",
        "dew_point_2m",
        "apparent_temperature",
        "is_day",
        "precipitation",
        "precipitation_probability",
        "snowfall",
        "rain",
        "showers",
        "snow_depth",
        "wind_speed_10m",
        "wind_speed_80m",
        "wind_speed_120m",
        "wind_speed_180m",
        "wind_direction_10m",
        "wind_direction_80m",
        "wind_direction_120m",
        "wind_direction_180m",
        "wind_gusts_10m",
        "visibility",
        "cloud_cover",
        "cloud_cover_low",
        "cloud_cover_mid",
        "cloud_cover_high",
        "pressure_msl",
        "surface_pressure",
        "shortwave_radiation",
        "direct_radiation",
        "direct_normal_irradiance",
        "global_tilted_irradiance",
        "global_tilted_irradiance_instant",
        "diffuse_radiation",
        "sunshine_duration",
        "soil_temperature_0cm",
        "soil_temperature_6cm",
        "soil_temperature_18cm",
        "soil_temperature_54cm",
        "soil_moisture_0_to_1cm",
        "soil_moisture_1_to_3cm",
        "soil_moisture_3_to_9cm",
        "soil_moisture_9_to_27cm",
        "soil_moisture_27_to_81cm",
        "lightning_potential",
        "freezing_level_height",
        "cape",
        "vapour_pressure_deficit",
        "evapotranspiration",
        "et0_fao_evapotranspiration"
    )
}

object OpenMeteoForecastParameters {
    val parameters: List<String> = listOf(
        "weather_code",
        "temperature_2m_max",
        "temperature_2m_min",
        "sunset",
        "sunrise",
        "apparent_temperature_max",
        "apparent_temperature_min",
        "wind_speed_10m_max",
        "wind_direction_10m_dominant",
        "precipitation_probability_max"
    )
}

object OpenMeteoHourlyParameters {
    val parameters: List<String> = listOf(
        "weather_code",
        "relative_humidity_2m",
        "dew_point_2m",
        "apparent_temperature",
        "precipitation_probability",
        "wind_speed_10m",
        "wind_speed_80m",
        "wind_speed_120m",
        "wind_speed_180m",
        "wind_direction_10m",
        "wind_direction_80m",
        "wind_direction_120m",
        "wind_direction_180m",
        "temperature_80m",
        "temperature_120m",
        "temperature_180m",
        "temperature_2m"
    )
}

@Serializable
data class OpenMeteoCurrentResponse(
    @SerialName("current") val currentWeather: OpenMeteoCurrentWeather? = null
)

@Serializable
data class OpenMeteoCurrentWeather(
    @SerialName("weather_code") val code: Int,
    @SerialName("temperature_2m") val temperature: Double,
    @SerialName("temperature_80m") val temperature80m: Double,
    @SerialName("temperature_120m") val temperature120m: Double,
    @SerialName("temperature_180m") val temperature180m: Double,
    @SerialName("relative_humidity_2m") val relativeHumidity2m: Int,
    @SerialName("dew_point_2m") val dewPoint2m: Double,
    @SerialName("apparent_temperature") val felt: Double,
    @SerialName("is_day") val isDay: Int,
    @SerialName("precipitation") val precipitation: Double,
    @SerialName("precipitation_probability") val precipitationProbability: Int,
    @SerialName("snowfall") val snowfall: Double,
    @SerialName("rain") val rain: Double,
    @SerialName("showers") val showers: Double,
    @SerialName("snow_depth") val snowDepth: Double,
    @SerialName("wind_speed_10m") val windSpeed: Double,
    @SerialName("wind_speed_80m") val windSpeed80m: Double,
    @SerialName("wind_speed_120m") val windSpeed120m: Double,
    @SerialName("wind_speed_180m") val windSpeed180m: Double,
    @SerialName("wind_gusts_10m") val windGusts10m: Double,

    @SerialName("wind_direction_10m") val windDirection: Int,
    @SerialName("wind_direction_80m") val windDirection80m: Int,
    @SerialName("wind_direction_120m") val windDirection120m: Int,
    @SerialName("wind_direction_180m") val windDirection180m: Int,

    @SerialName("visibility") val visibility: Double,
    @SerialName("cloud_cover") val cloud: Int,
    @SerialName("cloud_cover_low") val cloudCoverLow: Int,
    @SerialName("cloud_cover_mid") val cloudCoverMid: Int,
    @SerialName("cloud_cover_high") val cloudCoverHigh: Int,

    @SerialName("pressure_msl") val seaPressure: Double,
    @SerialName("surface_pressure") val pressure: Double,

    @SerialName("shortwave_radiation") val shortwaveRadiation: Double,
    @SerialName("direct_radiation") val directRadiation: Double,
    @SerialName("direct_normal_irradiance") val directNormalIrradiance: Double,
    @SerialName("global_tilted_irradiance") val globalTiltedIrradiance: Double,
    @SerialName("global_tilted_irradiance_instant") val globalTiltedIrradianceInstant: Double,
    @SerialName("diffuse_radiation") val diffuseRadiation: Double,
    @SerialName("sunshine_duration") val sunshineDuration: Double,

    @SerialName("soil_temperature_0cm") val soilTemperature0cm: Double,
    @SerialName("soil_temperature_6cm") val soilTemperature6cm: Double,
    @SerialName("soil_temperature_18cm") val soilTemperature18cm: Double,
    @SerialName("soil_temperature_54cm") val soilTemperature54cm: Double,

    @SerialName("soil_moisture_0_to_1cm") val soilMoisture0to1cm: Double,
    @SerialName("soil_moisture_1_to_3cm") val soilMoisture1to3cm: Double,
    @SerialName("soil_moisture_3_to_9cm") val soilMoisture3to9cm: Double,
    @SerialName("soil_moisture_9_to_27cm") val soilMoisture9to27cm: Double,
    @SerialName("soil_moisture_27_to_81cm") val soilMoisture27to81cm: Double,

    @SerialName("lightning_potential") val lightningPotential: Double?,
    @SerialName("freezing_level_height") val freezingLevelHeight: Double,
    @SerialName("cape") val cape: Double,
    @SerialName("vapour_pressure_deficit") val vapourPressureDeficit: Double,

    @SerialName("evapotranspiration") val evapotranspiration: Double,
    @SerialName("et0_fao_evapotranspiration") val et0FaoEvapotranspiration: Double
) {
    @OptIn(ExperimentalTime::class)
    fun toWeatherData(
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        timestamp: Long = Clock.System.now().toEpochMilliseconds(),
        provider: String = OpenMeteoProvider.NAME,
        lastUpdate: Long = 0L,
        description: String = "Current weather",
        name: String = OpenMeteoProvider.NAME,
        update: Long = Clock.System.now().toEpochMilliseconds()
    ): WeatherData {
        return WeatherData(
            latitude = latitude,
            longitude = longitude,
            timestamp = timestamp,

            // Температуры
            temperature = temperature,
            temperature80m = temperature80m,
            temperature120m = temperature120m,
            temperature180m = temperature180m,

            // Ощущается как
            felt = felt,

            // Влажность → проценты
            humidity = relativeHumidity2m,

            // День/ночь как в Open-Meteo (1/0)
            isDay = isDay,

            // Осадки и вероятность
            precipitation = precipitation,
            precipitationProbability = precipitationProbability,

            // Погодный код и облачность
            code = WeatherCode.from(code),
            cloud = cloud,
            cloudLow = cloudCoverLow,
            cloudMid = cloudCoverMid,
            cloudHigh = cloudCoverHigh,

            // Давление (мм рт. ст.)
            seaPressure = seaPressure/1.3,
            pressure = pressure/1.3,

            // Ветер 10 м — совместимо со старыми полями
            windSpeed = windSpeed,
            windDirection = windDirection,

            // Ветер на высотах
            windSpeed80m = windSpeed80m,
            windSpeed120m = windSpeed120m,
            windSpeed180m = windSpeed180m,
            windDirection80m = windDirection80m,
            windDirection120m = windDirection120m,
            windDirection180m = windDirection180m,
            windGusts10m = windGusts10m,

            // Видимость
            visibility = visibility.toInt(),

            // Осадки разных типов
            snowPrecipitation = snowfall,
            snowCover = snowDepth,
            rainPrecipitation = rain,
            showerPrecipitation = showers,

            // Радиация/энергия
            shortwaveRadiation = shortwaveRadiation,
            directRadiation = directRadiation,
            directNormalIrradiance = directNormalIrradiance,
            globalTiltedIrradiance = globalTiltedIrradiance,
            globalTiltedIrradianceInstant = globalTiltedIrradianceInstant,
            diffuseRadiation = diffuseRadiation,
            sunshineDuration = sunshineDuration,

            // Почва: температура
            soilTemperature0cm = soilTemperature0cm,
            soilTemperature6cm = soilTemperature6cm,
            soilTemperature18cm = soilTemperature18cm,
            soilTemperature54cm = soilTemperature54cm,

            // Почва: влажность (как есть, в м³/м³)
            soilMoisture0to1cm = soilMoisture0to1cm,
            soilMoisture1to3cm = soilMoisture1to3cm,
            soilMoisture3to9cm = soilMoisture3to9cm,
            soilMoisture9to27cm = soilMoisture9to27cm,
            soilMoisture27to81cm = soilMoisture27to81cm,

            // Прочее
            dewPoint2m = dewPoint2m,
            lightningPotential = lightningPotential,
            freezingLevelHeight = freezingLevelHeight,
            cape = cape,
            vapourPressureDeficit = vapourPressureDeficit,
            evapotranspiration = evapotranspiration,
            et0FaoEvapotranspiration = et0FaoEvapotranspiration,

            // Метаданные
            locality = "Неизвестно",
            provider = provider,
            lastUpdate = lastUpdate,
            description = description,

            name = name,
            update = update
        )
    }
}

@Serializable
data class OpenMeteoForecastResponse(
    @SerialName("daily_units") val units: OpenMeteoForecastUnit,
    @SerialName("daily") val data: OpenMeteoForecastData
) {
    @OptIn(ExperimentalTime::class)
    fun toForecastList(name: String = "Default"): ForecastList {
        val list = data.time.indices.map { index ->
            ForecastData(
                timestamp = timeParse(data.time.getOrNull(index)),
                code = WeatherCode.from(data.weatherCode.getOrNull(index) ?: -1),
                temperatureMax = data.temperature2mMax.getOrNull(index),
                temperatureMin = data.temperature2mMin.getOrNull(index),
                feltMax = data.feltMax.getOrNull(index),
                feltMin = data.feltMin.getOrNull(index),
                windSpeed = data.windSpeed.getOrNull(index),
                windDirection = data.windDirection.getOrNull(index),
                precipitation = data.precipitation.getOrNull(index),
                sunrise = timeParse(data.sunrise.getOrNull(index)),
                sunset = timeParse(data.sunset.getOrNull(index))
            )
        }
        return ForecastList(list, Clock.System.now().toEpochMilliseconds())
    }

    @OptIn(ExperimentalTime::class)
    private fun timeParse(time: String?): Long? =
        try {
            time?.let {
                runCatching {
                    LocalDateTime.parse(time)
                        .toInstant(TimeZone.currentSystemDefault())
                        .toEpochMilliseconds()
                }.getOrElse {
                    runCatching {
                        LocalDate.parse(time)
                            .atStartOfDayIn(TimeZone.currentSystemDefault())
                            .toEpochMilliseconds()
                    }.getOrNull()
                }
            }
        } catch (e: Exception) {
            println(e)
            null
        }
}

@Serializable
data class OpenMeteoForecastUnit(
    @SerialName("time") val time: String,
    @SerialName("weather_code") val weatherCode: String,
    @SerialName("temperature_2m_max") val temperature2mMax: String,
    @SerialName("temperature_2m_min") val temperature2mMin: String,
    @SerialName("apparent_temperature_max") val feltMax: String,
    @SerialName("apparent_temperature_min") val feltMin: String,
    @SerialName("wind_speed_10m_max") val windSpeed: String,
    @SerialName("wind_direction_10m_dominant") val windDirection: String,
    @SerialName("precipitation_probability_max") val precipitation: String,
    @SerialName("sunrise") val sunrise: String,
    @SerialName("sunset") val sunset: String,
)

@Serializable
data class OpenMeteoForecastData(
    @SerialName("time") val time: List<String>,
    @SerialName("weather_code") val weatherCode: List<Int>,
    @SerialName("temperature_2m_max") val temperature2mMax: List<Double>,
    @SerialName("temperature_2m_min") val temperature2mMin: List<Double>,
    @SerialName("apparent_temperature_max") val feltMax: List<Double>,
    @SerialName("apparent_temperature_min") val feltMin: List<Double>,
    @SerialName("wind_speed_10m_max") val windSpeed: List<Double>,
    @SerialName("wind_direction_10m_dominant") val windDirection: List<Int>,
    @SerialName("precipitation_probability_max") val precipitation: List<Double>,
    @SerialName("sunrise") val sunrise: List<String>,
    @SerialName("sunset") val sunset: List<String>,
)

@Serializable
data class OpenMeteoHourlyResponse(
    @SerialName("hourly_units") val units: OpenMeteoHourlyUnit,
    @SerialName("hourly") val data: OpenMeteoHourlyData
) {
    @OptIn(ExperimentalTime::class)
    fun toHourlyList(): HourlyList {
        val list = data.time.indices.map { index ->
            HourlyData(
                timestamp = timeParse(data.time.getOrNull(index)),
                temperature = data.temperature2m.getOrNull(index),
                temperature80m = data.temperature80m.getOrNull(index),
                temperature120m = data.temperature120m.getOrNull(index),
                temperature180m = data.temperature180m.getOrNull(index),
                apparentTemperature = data.apparentTemperature.getOrNull(index),
                dewPoint = data.dewPoint.getOrNull(index),
                humidity = data.humidity.getOrNull(index),
                windSpeed10m = data.windSpeed10m.getOrNull(index),
                windSpeed80m = data.windSpeed80m.getOrNull(index),
                windSpeed120m = data.windSpeed120m.getOrNull(index),
                windSpeed180m = data.windSpeed180m.getOrNull(index),
                windDirection10m = data.windDirection10m.getOrNull(index),
                windDirection80m = data.windDirection80m.getOrNull(index),
                windDirection120m = data.windDirection120m.getOrNull(index),
                windDirection180m = data.windDirection180m.getOrNull(index),
                precipitationProbability = data.precipitationProbability.getOrNull(index),
                code = WeatherCode.from(data.weatherCode.getOrNull(index) ?: -1)
            )
        }
        return HourlyList(list, Clock.System.now().toEpochMilliseconds())
    }

    @OptIn(ExperimentalTime::class)
    private fun timeParse(time: String?): Long? =
        try {
            time?.let {
                runCatching {
                    LocalDateTime.parse(time)
                        .toInstant(TimeZone.currentSystemDefault())
                        .toEpochMilliseconds()
                }.getOrNull()
            }
        } catch (e: Exception) {
            println(e)
            null
        }
}

@Serializable
data class OpenMeteoHourlyUnit(
    @SerialName("time") val time: String,
    @SerialName("relative_humidity_2m") val humidity: String,
    @SerialName("dew_point_2m") val dewPoint: String,
    @SerialName("apparent_temperature") val apparentTemperature: String,
    @SerialName("precipitation_probability") val precipitationProbability: String,
    @SerialName("wind_speed_10m") val windSpeed10m: String,
    @SerialName("wind_speed_80m") val windSpeed80m: String,
    @SerialName("wind_speed_120m") val windSpeed120m: String,
    @SerialName("wind_speed_180m") val windSpeed180m: String,
    @SerialName("wind_direction_10m") val windDirection10m: String,
    @SerialName("wind_direction_80m") val windDirection80m: String,
    @SerialName("wind_direction_120m") val windDirection120m: String,
    @SerialName("wind_direction_180m") val windDirection180m: String,
    @SerialName("weather_code") val weatherCode: String,
    @SerialName("temperature_80m") val temperature80m: String,
    @SerialName("temperature_120m") val temperature120m: String,
    @SerialName("temperature_180m") val temperature180m: String,
    @SerialName("temperature_2m") val temperature2m: String,
)

@Serializable
data class OpenMeteoHourlyData(
    @SerialName("time") val time: List<String>,
    @SerialName("relative_humidity_2m") val humidity: List<Int>,
    @SerialName("dew_point_2m") val dewPoint: List<Double>,
    @SerialName("apparent_temperature") val apparentTemperature: List<Double>,
    @SerialName("precipitation_probability") val precipitationProbability: List<Int>,
    @SerialName("wind_speed_10m") val windSpeed10m: List<Double>,
    @SerialName("wind_speed_80m") val windSpeed80m: List<Double>,
    @SerialName("wind_speed_120m") val windSpeed120m: List<Double>,
    @SerialName("wind_speed_180m") val windSpeed180m: List<Double>,
    @SerialName("wind_direction_10m") val windDirection10m: List<Int>,
    @SerialName("wind_direction_80m") val windDirection80m: List<Int>,
    @SerialName("wind_direction_120m") val windDirection120m: List<Int>,
    @SerialName("wind_direction_180m") val windDirection180m: List<Int>,
    @SerialName("weather_code") val weatherCode: List<Int>,
    @SerialName("temperature_80m") val temperature80m: List<Double>,
    @SerialName("temperature_120m") val temperature120m: List<Double>,
    @SerialName("temperature_180m") val temperature180m: List<Double>,
    @SerialName("temperature_2m") val temperature2m: List<Double>,
)