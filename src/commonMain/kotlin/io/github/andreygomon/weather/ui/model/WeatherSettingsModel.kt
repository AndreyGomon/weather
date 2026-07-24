package io.github.andreygomon.weather.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.andreygomon.weather.api.WeatherApi
import io.github.andreygomon.weather.data.WeatherSettings
import io.github.andreygomon.weather.provider.WeatherProviderFactory

class WeatherSettingsModel(
    private val api: WeatherApi,
) : ViewModel() {
    val providers: List<String> = WeatherProviderFactory.getProviderNames()

    var uiState by mutableStateOf(
        WeatherSettingsUiState(
            provider = api.settings.value.provider.ifEmpty {
                providers.firstOrNull().orEmpty()
            },
            source = api.settings.value.source,
        ),
    )
        private set

    fun updateProvider(provider: String) {
        uiState = uiState.copy(provider = provider).validated()
        api.updateSettings(
            WeatherSettings(
                provider = uiState.provider,
                source = uiState.source,
            ),
        )
    }

    fun updateSource(source: String) {
        uiState = uiState.copy(source = source).validated()
        api.updateSettings(
            WeatherSettings(
                provider = uiState.provider,
                source = uiState.source,
            ),
        )
    }
}

data class WeatherSettingsUiState(
    val provider: String,
    val source: String,
    val isValid: Boolean = true,
) {
    fun validated(): WeatherSettingsUiState {
        val normalizedProvider = provider.trim()
        return copy(
            provider = normalizedProvider,
            isValid = normalizedProvider.isNotEmpty(),
        )
    }
}
