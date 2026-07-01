package ru.mascot.features.weather.plugin.editor

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.oscim.core.GeoPoint
import ru.mascot.features.objects.presentation.contract.PayloadEditor
import ru.onyx.map.coordinates.presentation.CoordinateBox
import ru.mascot.features.weather.ui.screen.WeatherSettings
import ru.mascot.features.weather.plugin.WeatherPayload
import ru.mascot.features.weather.plugin.WeatherType

@Single(binds = [PayloadEditor::class])
@Named(type = WeatherType::class)
internal class WeatherEditor : PayloadEditor<WeatherPayload> {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(
        current: WeatherPayload,
        onChange: (WeatherPayload) -> Unit,
        decorationBox: @Composable ((@Composable (() -> Unit)) -> Unit)
    ) = decorationBox {
        WeatherSettings(current, onChange)
        CoordinateBox(
            geoPoint = current.geoPoint ?: GeoPoint(0,0),
            onChange = { newPoint ->
                onChange(current.copy(geoPoint = newPoint))
            },
            onDelete = null
        )
    }
}
