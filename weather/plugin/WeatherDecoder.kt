package ru.mascot.features.weather.plugin

import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.oscim.core.GeoPoint
import ru.mascot.features.ProtoWeatherData
import ru.mascot.features.ProtoWeatherObject
import ru.mascot.features.WeatherProto
import ru.mascot.features.objects.data.mapper.toGeoPoint
import ru.mascot.features.objects.domain.model.contract.InternalDecoder
import ru.mascot.features.weather.data.WeatherCode
import ru.mascot.features.weather.data.WeatherData

@Single(binds = [InternalDecoder::class])
@Named(type = WeatherType::class)
internal class WeatherDecoder : InternalDecoder<WeatherPayload> {
    override fun decode(data: ByteArray): WeatherPayload {
        val proto = ProtoWeatherObject.ADAPTER.decode(data)
        return WeatherPayload(
            geoPoint = proto.point?.toGeoPoint() ?: GeoPoint(0.0, 0.0),
            provider = proto.provider,
            data = proto.data_?.toWeatherData() ?: WeatherData(),
            isInitialized = true
        )
    }
}