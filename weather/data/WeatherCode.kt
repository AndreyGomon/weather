package ru.mascot.features.weather.data

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.mascot.features.ui.icons.*
import androidx.compose.ui.unit.Density
import org.jetbrains.compose.resources.DrawableResource
import ru.mascot.features.resources.Res
import ru.mascot.features.resources.cloudly
import ru.mascot.features.resources.day_clear
import ru.mascot.features.resources.day_drizzle
import ru.mascot.features.resources.day_fog
import ru.mascot.features.resources.day_hail
import ru.mascot.features.resources.day_heavy_drizzle
import ru.mascot.features.resources.day_heavy_rain
import ru.mascot.features.resources.day_heavy_shower_rain
import ru.mascot.features.resources.day_heavy_snow
import ru.mascot.features.resources.day_heavy_thunder
import ru.mascot.features.resources.day_ice_fog
import ru.mascot.features.resources.day_light_drizzle
import ru.mascot.features.resources.day_light_hail
import ru.mascot.features.resources.day_light_rain
import ru.mascot.features.resources.day_light_shower_rain
import ru.mascot.features.resources.day_light_snow
import ru.mascot.features.resources.day_light_thunder
import ru.mascot.features.resources.day_mainly_clear
import ru.mascot.features.resources.day_partly_cloud
import ru.mascot.features.resources.day_rain
import ru.mascot.features.resources.day_shower_rain
import ru.mascot.features.resources.day_snow
import ru.mascot.features.resources.day_thunder
import ru.mascot.features.resources.hail
import ru.mascot.features.resources.night_drizzle
import ru.mascot.features.resources.night_fog
import ru.mascot.features.resources.night_hail
import ru.mascot.features.resources.night_heavy_drizzle
import ru.mascot.features.resources.night_heavy_rain
import ru.mascot.features.resources.night_heavy_shower_rain
import ru.mascot.features.resources.night_heavy_snow
import ru.mascot.features.resources.night_heavy_thunder
import ru.mascot.features.resources.night_ice_fog
import ru.mascot.features.resources.night_light_drizzle
import ru.mascot.features.resources.night_light_hail
import ru.mascot.features.resources.night_light_rain
import ru.mascot.features.resources.night_light_shower_rain
import ru.mascot.features.resources.night_light_snow
import ru.mascot.features.resources.night_light_thunder
import ru.mascot.features.resources.night_mainly_clear
import ru.mascot.features.resources.night_partly_cloud
import ru.mascot.features.resources.night_rain
import ru.mascot.features.resources.night_shower_rain
import ru.mascot.features.resources.night_snow
import ru.mascot.features.resources.night_thunder
import ru.mascot.features.resources.snowstorm
import ru.mascot.features.resources.unknown

object WeatherCodeSerializer : KSerializer<WeatherCode> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("WeatherCode", PrimitiveKind.SHORT)

    override fun serialize(encoder: Encoder, value: WeatherCode) = encoder.encodeShort(value.code)
    override fun deserialize(decoder: Decoder): WeatherCode {
        val code = decoder.decodeFloat().toInt().toShort()
        return WeatherCode.from(code)
    }
}

@Serializable(with = WeatherCodeSerializer::class)
enum class WeatherCode(
    val code: Short,
    val message: String
) {
    @SerialName("Soil")
    Soil(-12, "Почва"),
    @SerialName("UVIndex")
    UVIndex(-11, "Солнечная радиация"),
    @SerialName("Precipitation")
    Precipitation(-10, "Осадки"),
    @SerialName("Pressure")
    Pressure(-9, "Давление"),
    @SerialName("Clouds_height")
    CloudsHeight(-8, "Облака и видимость"),
    @SerialName("Height")
    Height(-7, "Профиль по высоте"),
    @SerialName("CodeRed")
    CodeRed(-6, "Код красный"),
    @SerialName("CodeYellow")
    CodeYellow(-5, "Код желтый"),
    @SerialName("CodeGreen")
    CodeGreen(-4, "Код зеленый"),
    @SerialName("Wind")
    Wind(-3, "Ветер"),
    @SerialName("Humidity")
    Humidity(-2, "Влажность"),
    @SerialName("Unknown")
    Unknown(-1, "Неизвестно"),
    @SerialName("Clear")
    Clear(0, "Ясно"),
    @SerialName("MainlyClear")
    MainlyClear(1, "Преимущественно ясно"),
    @SerialName("PartlyCloudy")
    PartlyCloudy(2, "Небольшая облачность"),
    @SerialName("Overcast")
    Overcast(3, "Облачно"),
    @SerialName("Fog")
    Fog(45, "Туман"),
    @SerialName("DepositingRimeFog")
    DepositingRimeFog(48, "Ледяной туман"),
    @SerialName("DrizzleLight")
    DrizzleLight(51, "Легкая морось"),
    @SerialName("DrizzleModerate")
    DrizzleModerate(53, "Морось"),
    @SerialName("DrizzleDense")
    DrizzleDense(55, "Сильная морось"),
    @SerialName("FreezingDrizzleLight")
    FreezingDrizzleLight(56, "Легкая ледяная морось"),
    @SerialName("FreezingDrizzleDense")
    FreezingDrizzleDense(57, "Ледяная морось"),
    @SerialName("RainSlight")
    RainSlight(61, "Легкий дождь"),
    @SerialName("RainModerate")
    RainModerate(63, "Сильный дождь"),
    @SerialName("RainHeavy")
    RainHeavy(65, "Ливень"),
    @SerialName("FreezingRainLight")
    FreezingRainLight(66, "Легкий ледяной дождь"),
    @SerialName("FreezingRainHeavy")
    FreezingRainHeavy(67, "Ледяной дождь"),
    @SerialName("SnowFallSlight")
    SnowFallSlight(71, "Легкий снег"),
    @SerialName("SnowFallModerate")
    SnowFallModerate(73, "Снег"),
    @SerialName("SnowFallHeavy")
    SnowFallHeavy(75, "Снегопад"),
    @SerialName("SnowGrains")
    SnowGrains(77, "Град"),
    @SerialName("RainShowersSlight")
    RainShowersSlight(80, "Местами ливни"),
    @SerialName("RainShowersModerate")
    RainShowersModerate(81, "Умеренные ливни"),
    @SerialName("RainShowersViolent")
    RainShowersViolent(82, "Тропический ливень"),
    @SerialName("SnowShowersSlight")
    SnowShowersSlight(85, "Снегопад"),
    @SerialName("SnowShowersHeavy")
    SnowShowersHeavy(86, "Метель"),
    @SerialName("ThunderstormSlight")
    ThunderstormSlight(95, "Местами грозы"),
    @SerialName("ThunderstormSlightHail")
    ThunderstormSlightHail(96, "Грозы"),
    @SerialName("ThunderstormHeavyHail")
    ThunderstormHeavyHail(99, "Штормовые грозы");

    override fun toString(): String = message

    companion object {
        fun from(code: Short) = entries
            .firstOrNull { it.code == code } ?: Unknown

        fun from(code: Int) = from(code.toShort())
        fun from(code: Double) = from(code.toInt())


    }
}

object WindDirectionSerializer : KSerializer<WindDirection> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("WindDirection", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: WindDirection) {
        encoder.encodeInt(value.degree)
    }

    override fun deserialize(decoder: Decoder): WindDirection {
        val degree = decoder.decodeInt()
        return WindDirection.from(degree)
    }
}

@Serializable(with = WindDirectionSerializer::class)
enum class WindDirection(val degree: Int, val label: String) {
    @SerialName("NORTH")
    NORTH(0, "Северное"),
    @SerialName("NORTHEAST")
    NORTHEAST(45, "Северо-восточное"),
    @SerialName("EAST")
    EAST(90, "Восточное"),
    @SerialName("SOUTHEAST")
    SOUTHEAST(135, "Юго-восточное"),
    @SerialName("SOUTH")
    SOUTH(180, "Южное"),
    @SerialName("SOUTHWEST")
    SOUTHWEST(225, "Юго-западное"),
    @SerialName("WEST")
    WEST(270, "Западное"),
    @SerialName("NORTHWEST")
    NORTHWEST(315, "Северо-западное"),
    @SerialName("UNKNOWN")
    UNKNOWN(-1, "Неизвестно");

    companion object {
        fun from(degree: Int?): WindDirection {
            return degree?.let {
                if (degree == -1) UNKNOWN
                val normalized = ((degree % 360) + 360) % 360

                 when (normalized) {
                    in 337..360, in 0..22 -> NORTH
                    in 22..67 -> NORTHEAST
                    in 67..112 -> EAST
                    in 112..157 -> SOUTHEAST
                    in 157..202 -> SOUTH
                    in 202..247 -> SOUTHWEST
                    in 247..292 -> WEST
                    in 292..336 -> NORTHWEST
                    else -> UNKNOWN
                }
            } ?: UNKNOWN
        }
    }

    override fun toString(): String = label
}


object WeatherAnimations {
    enum class AnimationType(val suffix: String) {
        MAIN("_m"),
        SIMPLE("_s")
    }

    private data class AnimationPair(val day: String, val night: String) {
        fun pick(isDay: Boolean) = if (isDay) day else night
    }

    private fun pair(day: String, night: String = day) = AnimationPair(day, night)

    private val gifMap: Map<WeatherCode, AnimationPair> = mapOf(
        WeatherCode.Unknown                to pair("files/json/unknown"),

        WeatherCode.Clear                  to pair("files/json/day_clear",  "files/json/night_clear"),
        WeatherCode.MainlyClear            to pair("files/json/day_mainly_clear",  "files/json/night_mainly_clear"),

        WeatherCode.PartlyCloudy           to pair("files/json/day_partly_cloud",       "files/json/night_partly_cloud"),
        WeatherCode.Overcast               to pair("files/json/cloudy", "files/json/cloudy"),

        WeatherCode.Fog                    to pair("files/json/day_fog",         "files/json/night_fog"),
        WeatherCode.DepositingRimeFog      to pair("files/json/day_fog_ice",     "files/json/night_fog_ice"),
        WeatherCode.DrizzleLight           to pair("files/json/day_drizzle_light","files/json/night_drizzle_light"),
        WeatherCode.FreezingDrizzleLight   to pair("files/json/hail",         "files/json/night_fog"),
        WeatherCode.DrizzleModerate        to pair("files/json/day_drizzle",         "files/json/night_drizzle"),

        WeatherCode.DrizzleDense           to pair("files/json/day_drizzle_heavy",        "files/json/night_drizzle_heavy"),
        WeatherCode.FreezingDrizzleDense   to pair("files/json/hail",        "files/json/hail"),
        WeatherCode.RainModerate           to pair("files/json/day_rain",        "files/json/night_rain"),
        WeatherCode.FreezingRainLight      to pair("files/json/day_hail_light",        "files/json/night_hail_light"),
        WeatherCode.FreezingRainHeavy      to pair("files/json/day_hail",        "files/json/night_hail"),

        WeatherCode.RainSlight             to pair("files/json/day_rain_light",  "files/json/night_rain_light"),
        WeatherCode.RainShowersSlight      to pair("files/json/day_rain_shower_light",  "files/json/night_rain_shower_light"),

        WeatherCode.RainHeavy              to pair("files/json/day_rain_heavy",     "files/json/night_rain_heavy"),
        WeatherCode.RainShowersViolent     to pair("files/json/day_rain_shower_heavy",     "files/json/night_rain_shower_heavy"),
        WeatherCode.ThunderstormSlight     to pair("files/json/day_thunder_light",     "files/json/night_thunder_light"),
        WeatherCode.ThunderstormSlightHail to pair("files/json/day_thunder",     "files/json/night_thunder"),
        WeatherCode.ThunderstormHeavyHail  to pair("files/json/day_thunder_heavy",     "files/json/night_thunder_heavy"),

        WeatherCode.SnowFallSlight         to pair("files/json/day_snow_light",        "files/json/night_snow_light"),
        WeatherCode.SnowFallModerate       to pair("files/json/day_snow",        "files/json/night_snow"),
        WeatherCode.SnowFallHeavy          to pair("files/json/day_snow_heavy",        "files/json/night_snow_heavy"),
        WeatherCode.SnowGrains             to pair("files/json/hail",        "files/json/hail"),
        WeatherCode.RainShowersModerate    to pair("files/json/day_rain_shower",        "files/json/night_rain_shower"),
        WeatherCode.SnowShowersSlight      to pair("files/json/snowstorm",        "files/json/snowstorm"),
        WeatherCode.SnowShowersHeavy       to pair("files/json/snowstorm",        "files/json/snowstorm"),


        WeatherCode.Humidity               to pair("files/json/humidity", "files/json/humidity"),
        WeatherCode.Wind                   to pair("files/json/wind", "files/json/wind"),
        WeatherCode.CodeGreen              to pair("files/json/code_green", "files/json/code_green"),
        WeatherCode.CodeYellow             to pair("files/json/code_yellow", "files/json/code_yellow"),
        WeatherCode.CodeRed                to pair("files/json/code_green", "files/json/code_green"),
        WeatherCode.Height                 to pair("files/json/pressure_high", "files/json/pressure_high"),
        WeatherCode.CloudsHeight           to pair("files/json/cloud_height", "files/json/cloud_height"),
        WeatherCode.Pressure               to pair("files/json/barometer", "files/json/barometer"),
        WeatherCode.Precipitation          to pair("files/json/precipitation", "files/json/precipitation"),
        WeatherCode.UVIndex                to pair("files/json/uv_index", "files/json/uv_index"),
        WeatherCode.Soil                   to pair("files/json/soil", "files/json/soil")
    )

    fun gif(type: AnimationType, code: WeatherCode, isDay: Boolean): String {
        val suffix = type.suffix
        val basePair = gifMap[code] ?: return "files/json/unknown$suffix.json"
        return basePair.pick(isDay) + suffix + ".json"
    }
}

object WeatherIcons {
    private data class IconPair(val day: DrawableResource, val night: DrawableResource) {
        fun pick(isDay: Boolean): DrawableResource = if (isDay) day else night
    }

    private fun pair(day: DrawableResource, night: DrawableResource = day) = IconPair(day, night)

    private val map: Map<WeatherCode, IconPair> = mapOf(
        WeatherCode.Unknown                to pair(Res.drawable.unknown),

        WeatherCode.Clear                  to pair(Res.drawable.day_clear,            Res.drawable.day_clear),
        WeatherCode.MainlyClear            to pair(Res.drawable.day_mainly_clear,      Res.drawable.night_mainly_clear),
        WeatherCode.PartlyCloudy           to pair(Res.drawable.day_partly_cloud,      Res.drawable.night_partly_cloud),
        WeatherCode.Overcast               to pair(Res.drawable.cloudly),

        WeatherCode.Fog                    to pair(Res.drawable.day_fog,              Res.drawable.night_fog),
        WeatherCode.DepositingRimeFog      to pair(Res.drawable.day_ice_fog,           Res.drawable.night_ice_fog),

        WeatherCode.DrizzleLight           to pair(Res.drawable.day_light_drizzle,     Res.drawable.night_light_drizzle),
        WeatherCode.DrizzleModerate        to pair(Res.drawable.day_drizzle,          Res.drawable.night_drizzle),
        WeatherCode.DrizzleDense           to pair(Res.drawable.day_heavy_drizzle,     Res.drawable.night_heavy_drizzle),
        WeatherCode.FreezingDrizzleLight   to pair(Res.drawable.hail),
        WeatherCode.FreezingDrizzleDense   to pair(Res.drawable.hail),

        WeatherCode.RainSlight             to pair(Res.drawable.day_light_rain,        Res.drawable.night_light_rain),
        WeatherCode.RainModerate           to pair(Res.drawable.day_rain,             Res.drawable.night_rain),
        WeatherCode.RainHeavy              to pair(Res.drawable.day_heavy_rain,        Res.drawable.night_heavy_rain),

        WeatherCode.FreezingRainLight      to pair(Res.drawable.day_light_hail,        Res.drawable.night_light_hail),
        WeatherCode.FreezingRainHeavy      to pair(Res.drawable.day_hail,             Res.drawable.night_hail),

        WeatherCode.SnowFallSlight         to pair(Res.drawable.day_light_snow,        Res.drawable.night_light_snow),
        WeatherCode.SnowFallModerate       to pair(Res.drawable.day_snow,             Res.drawable.night_snow),
        WeatherCode.SnowFallHeavy          to pair(Res.drawable.day_heavy_snow,        Res.drawable.night_heavy_snow),
        WeatherCode.SnowGrains             to pair(Res.drawable.hail),

        WeatherCode.RainShowersSlight      to pair(Res.drawable.day_light_shower_rain,  Res.drawable.night_light_shower_rain),
        WeatherCode.RainShowersModerate    to pair(Res.drawable.day_shower_rain,       Res.drawable.night_shower_rain),
        WeatherCode.RainShowersViolent     to pair(Res.drawable.day_heavy_shower_rain,  Res.drawable.night_heavy_shower_rain),

        WeatherCode.SnowShowersSlight      to pair(Res.drawable.snowstorm),
        WeatherCode.SnowShowersHeavy       to pair(Res.drawable.snowstorm),

        WeatherCode.ThunderstormSlight     to pair(Res.drawable.day_light_thunder,     Res.drawable.night_light_thunder),
        WeatherCode.ThunderstormSlightHail to pair(Res.drawable.day_thunder,          Res.drawable.night_thunder),
        WeatherCode.ThunderstormHeavyHail  to pair(Res.drawable.day_heavy_thunder,     Res.drawable.night_heavy_thunder),
    )

    private val fallback = pair(Res.drawable.unknown)

    fun icon(code: WeatherCode, isDay: Boolean): DrawableResource =
        (map[code] ?: fallback).pick(isDay)
}
