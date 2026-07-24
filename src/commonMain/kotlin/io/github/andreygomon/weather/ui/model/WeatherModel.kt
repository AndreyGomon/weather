package io.github.andreygomon.weather.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.andreygomon.weather.api.WeatherApi
import io.github.andreygomon.weather.data.HourlyData
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.data.WeatherSettings
import io.github.andreygomon.weather.data.error.ResultWrapper
import io.github.andreygomon.weather.model.Coordinates
import io.github.andreygomon.weather.provider.WeatherProviderFactory
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WeatherModel(
    private val api: WeatherApi,
) : ViewModel() {
    private var onWeatherUpdate: ((WeatherData) -> Unit)? = null
    private var onLocationUpdate: ((Coordinates) -> Unit)? = null

    var uiState by mutableStateOf(
        WeatherUiState(
            providers = WeatherProviderFactory.getProviderNames(),
            sources = WeatherProviderFactory.LocationSource.getSources(),
        ),
    )
        private set

    init {
        viewModelScope.launch {
            api.currentWeather.collect { data ->
                val location = uiState.location
                    ?: Coordinates(data.latitude, data.longitude)

                uiState = uiState.copy(
                    weather = data,
                    location = location,
                )

                onLocationUpdate?.invoke(location)
                onWeatherUpdate?.invoke(data)
            }
        }

        viewModelScope.launch {
            combine(api.settings, api.stats) { settings, stats ->
                val stat = stats.find { it.provider == settings.provider }
                val provider = WeatherProviderFactory.getProvider(settings.provider)
                val remaining = provider?.getRemaining(
                    provider.perDay,
                    stat?.requests ?: 0L,
                )
                settings to remaining
            }.collect { (settings, remaining) ->
                uiState = uiState.copy(
                    provider = settings.provider,
                    source = settings.source,
                    remaining = remaining,
                )
            }
        }
    }

    fun onWeatherUpdate(callback: (WeatherData) -> Unit) {
        onWeatherUpdate = callback
        callback(uiState.weather)
    }

    fun onLocationUpdate(callback: (Coordinates) -> Unit) {
        onLocationUpdate = callback
        uiState.location?.let(callback)
    }

    fun updateWeather() {
        val location = uiState.location ?: return
        viewModelScope.launch {
            when (
                val result = api.fetchWeather(
                    location = location,
                    provider = api.settings.value.provider,
                    current = true,
                )
            ) {
                is ResultWrapper.Success -> updateError(null)
                is ResultWrapper.Failure -> updateError(result.error.message)
            }
        }
    }

    fun updateHourly(data: HourlyData) {
        uiState = uiState.copy(hourly = data)
    }

    fun updateLocation(location: Coordinates, refresh: Boolean = false) {
        uiState = uiState.copy(location = location)
        onLocationUpdate?.invoke(location)
        if (refresh) updateWeather()
    }

    fun updateProvider(provider: String) {
        api.updateSettings(
            WeatherSettings(
                provider = provider,
                source = api.settings.value.source,
            ),
        )
    }

    fun updateSource(source: String) {
        api.updateSettings(
            WeatherSettings(
                provider = api.settings.value.provider,
                source = source,
            ),
        )
    }

    fun updateError(error: String?) {
        uiState = uiState.copy(error = error)
    }
}

data class WeatherUiState(
    val weather: WeatherData = WeatherData(),
    val hourly: HourlyData = HourlyData(),
    val provider: String = "",
    val providers: List<String> = emptyList(),
    val source: String = "",
    val sources: List<String> = emptyList(),
    val location: Coordinates? = null,
    val remaining: Long? = null,
    val error: String? = null,
)
