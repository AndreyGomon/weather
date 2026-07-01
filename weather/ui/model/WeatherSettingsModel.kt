package ru.mascot.features.weather.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.oscim.core.GeoPoint
import ru.mascot.features.weather.plugin.WeatherPayload
import ru.mascot.features.weather.provider.WeatherProviderFactory


class WeatherSettingsModel: ViewModel() {
    val providers: List<String> = WeatherProviderFactory.getProviderNames()
    var uiState by mutableStateOf(UiState(GeoPoint(0,0), providers.first()))
        private set

    fun init(payload: WeatherPayload) {
        uiState = UiState(
            point = payload.geoPoint,
            provider = payload.provider
        ).validated()
    }

    fun updateProvider(
        provider: String,
        payload: WeatherPayload,
        onChanged: (WeatherPayload) -> Unit
    ) {
        uiState = uiState.copy(
            provider = provider
        ).validated()

        onChanged(payload.copy(provider = provider))
    }
}

data class UiState(
    val point: GeoPoint?,
    val provider: String,
    val isValid: Boolean = true
) {
    fun validated(): UiState {
        return copy(isValid =  provider.isNotEmpty())
    }
}