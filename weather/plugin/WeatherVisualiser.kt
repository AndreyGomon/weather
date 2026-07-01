package ru.mascot.features.weather.plugin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import ru.mascot.features.objects.presentation.contract.PayloadVisualiser
import ru.mascot.features.ui.icons.FeaturesIcons
import ru.mascot.features.ui.icons.WeatherIcons23SvgrepoCom

@Single(binds = [PayloadVisualiser::class])
@Named(type = WeatherType::class)
internal class WeatherVisualiser : PayloadVisualiser<WeatherPayload> {
    @Composable
    override fun Icon(payload: WeatherPayload) {
        androidx.compose.material3.Icon(
            imageVector = FeaturesIcons.WeatherIcons23SvgrepoCom,
            contentDescription = null
        )
    }

    override suspend fun title(payload: WeatherPayload): String {
        return "Погода"
    }

    override suspend fun description(payload: WeatherPayload): String {
        return "Описание погоды"
    }
}