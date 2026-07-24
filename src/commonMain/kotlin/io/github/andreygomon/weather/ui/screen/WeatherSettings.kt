package io.github.andreygomon.weather.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.andreygomon.weather.provider.WeatherProviderFactory
import io.github.andreygomon.weather.ui.components.Dropdown
import io.github.andreygomon.weather.ui.model.WeatherSettingsModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeatherSettings(
    modifier: Modifier = Modifier,
    model: WeatherSettingsModel = koinViewModel(),
) {
    val state = model.uiState
    val provider = WeatherProviderFactory.getProvider(state.provider)
    val host = provider?.url
        ?.substringAfter("://", provider.url)
        ?.substringBefore('/')
        .orEmpty()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Dropdown(
            value = state.provider,
            values = model.providers,
            label = "Источник",
            onValueChanged = model::updateProvider,
            modifier = Modifier.fillMaxWidth(),
        )
        if (host.isNotEmpty()) {
            Text("Данные загружаются с: $host")
        }
    }
}
