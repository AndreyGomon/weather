package ru.mascot.features.weather.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.mascot.features.weather.plugin.WeatherPayload
import ru.mascot.features.weather.provider.WeatherProviderFactory
import ru.mascot.features.weather.ui.model.WeatherSettingsModel
import java.net.URI

@Composable
fun WeatherSettings(
    payload: WeatherPayload,
    onChange: (WeatherPayload) -> Unit,
    model: WeatherSettingsModel = viewModel()
) {
    LaunchedEffect(payload.geoPoint, payload.provider) {
        model.init(payload)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Provider(
            provider = payload.provider,
            providers = model.providers
        ) {
            model.updateProvider(it, payload, onChange)
        }
        Text(
            "Погода обновляется автоматически с хоста: " + URI(
                WeatherProviderFactory.getProvider(
                    payload.provider
                )?.url ?: ""
            ).host
        )
    }
}