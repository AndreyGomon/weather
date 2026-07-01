package ru.mascot.features.weather.plugin

import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.oscim.core.GeoPoint
import ru.mascot.features.objects.presentation.contract.PayloadValidator

@Single(binds = [PayloadValidator::class])
@Named(type = WeatherType::class)
internal class WeatherValidator : PayloadValidator<WeatherPayload> {
    override fun isValid(payload: WeatherPayload): Boolean {
        return geoPointValid(payload.geoPoint) &&
                providerValid(payload.provider)
    }

    private fun geoPointValid(geoPoint: GeoPoint?): Boolean = geoPoint != null
    private fun providerValid(provider: String): Boolean = provider.isNotEmpty()
}