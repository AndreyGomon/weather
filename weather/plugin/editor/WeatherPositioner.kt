package ru.mascot.features.weather.plugin.editor

import androidx.compose.runtime.Composable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import ru.mascot.features.objects.presentation.contract.PayloadPositioner
import ru.mascot.features.objects.presentation.contract.editaction.AddPointAction
import ru.mascot.features.objects.presentation.drawtypes.DrawType
import ru.mascot.features.weather.plugin.WeatherPayload
import ru.mascot.features.weather.plugin.WeatherType
import ru.onyx.map.toGeoPoint

@Single(binds = [PayloadPositioner::class])
@Named(type = WeatherType::class)
internal class WeatherPositioner : PayloadPositioner<WeatherPayload> {
    @Composable
    override fun Content(
        current: WeatherPayload,
        onChange: (WeatherPayload) -> Unit,
        onChangeAuxiliary: (List<DrawType>) -> Unit
    ) {
        AddPointAction { point ->
            onChange(current.copy(geoPoint = point.toGeoPoint()))
        }
    }
}