package ru.mascot.features.weather.plugin.editor

import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.oscim.core.GeoPoint
import ru.mascot.features.objects.presentation.contract.PayloadCreator
import ru.mascot.features.weather.data.WeatherRepository
import ru.mascot.features.weather.plugin.WeatherPayload
import ru.mascot.features.weather.plugin.WeatherType

@Single(binds = [PayloadCreator::class])
@Named(type = WeatherType::class)
internal class WeatherCreator : PayloadCreator<WeatherPayload> {
    override val fastCreate = {
        WeatherPayload(
            geoPoint = null,
            provider = WeatherRepository.settings.value.provider,
            isInitialized = false
        )
    }
}