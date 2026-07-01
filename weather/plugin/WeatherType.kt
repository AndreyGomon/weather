package ru.mascot.features.weather.plugin

import org.koin.core.annotation.Single
import ru.mascot.features.objects.domain.model.KnownTypes
import ru.mascot.features.objects.domain.model.contract.ObjectType

@Single
internal class WeatherType : ObjectType {
    override val id = KnownTypes.Weather.id
    override val name = "Погода"
}