package ru.mascot.features.weather.plugin

import org.koin.mp.KoinPlatform.getKoin
import org.oscim.core.GeoPoint
import ru.mascot.features.ProtoWeatherData
import ru.mascot.features.ProtoWeatherObject
import ru.mascot.features.WeatherProto
import ru.mascot.features.objects.data.mapper.toGeoPointProto
import ru.mascot.features.objects.domain.model.contract.ObjectPayload
import ru.mascot.features.objects.domain.model.contract.ObjectType
import ru.mascot.features.objects.domain.model.geometry.impl.SinglePointData
import ru.mascot.features.snail.SnailService
import ru.mascot.features.weather.api.WeatherApi
import ru.mascot.features.weather.data.WeatherCode
import ru.mascot.features.weather.data.WeatherData
import ru.mascot.features.weather.service.WeatherService

fun ProtoWeatherData.toWeatherData() =
    WeatherData(
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp,

        temperature = temperature,
        temperature80m = temperature80m,
        temperature120m = temperature120m,
        temperature180m = temperature180m,

        felt = felt,
        humidity = humidity,
        isDay = isDay,

        precipitation = precipitation,
        precipitationProbability = precipitationProbability,

        code = WeatherCode.from(code),
        cloud = cloud,
        cloudLow = cloudLow,
        cloudMid = cloudMid,
        cloudHigh = cloudHigh,

        seaPressure = seaPressure,
        pressure = pressure,

        windSpeed = windSpeed,
        windSpeed80m = windSpeed80m,
        windSpeed120m = windSpeed120m,
        windSpeed180m = windSpeed180m,
        windDirection = windDirection,
        windDirection80m = windDirection80m,
        windDirection120m = windDirection120m,
        windDirection180m = windDirection180m,
        windGusts10m = windGusts10m,

        visibility = visibility,

        shortwaveRadiation = shortwaveRadiation,
        directRadiation = directRadiation,
        directNormalIrradiance = directNormalIrradiance,
        globalTiltedIrradiance = globalTiltedIrradiance,
        globalTiltedIrradianceInstant = globalTiltedIrradianceInstant,
        diffuseRadiation = diffuseRadiation,
        sunshineDuration = sunshineDuration,

        soilTemperature0cm = soilTemperature0cm,
        soilTemperature6cm = soilTemperature6cm,
        soilTemperature18cm = soilTemperature18cm,
        soilTemperature54cm = soilTemperature54cm,

        soilMoisture0to1cm = soilMoisture0to1cm,
        soilMoisture1to3cm = soilMoisture1to3cm,
        soilMoisture3to9cm = soilMoisture3to9cm,
        soilMoisture9to27cm = soilMoisture9to27cm,
        soilMoisture27to81cm = soilMoisture27to81cm,

        dewPoint2m = dewPoint2m,
        lightningPotential = lightningPotential,
        freezingLevelHeight = freezingLevelHeight,
        cape = cape,
        vapourPressureDeficit = vapourPressureDeficit,
        evapotranspiration = evapotranspiration,
        et0FaoEvapotranspiration = et0FaoEvapotranspiration,

        locality = locality,
        provider = provider,
        lastUpdate = lastUpdate,
        description = description,

        name = name,
        update = update
    )

data class WeatherPayload(
    val geoPoint: GeoPoint?,
    val provider: String,
    val isInitialized: Boolean,
    val data: WeatherData = WeatherData()
) : ObjectPayload {
    init {
        getKoin().get<WeatherApi>()
        getKoin().get<WeatherService>()
    }

    override val type: ObjectType = WeatherType()
    override fun encode() = ProtoWeatherObject(
        point = geoPoint?.toGeoPointProto(),
        provider = provider,
        data_ = data.toProtobuf()
    ).encode()

    override val geometry = geoPoint?.let {
        listOf(SinglePointData(geoPoint))
    } ?: emptyList()
}