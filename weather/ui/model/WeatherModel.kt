package ru.mascot.features.weather.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import org.oscim.core.GeoPoint
import ovh.plrapps.mapcompose.api.scrollTo
import ovh.plrapps.mapcompose.ui.state.MapState
import ru.mascot.features.weather.data.HourlyData
import ru.mascot.features.weather.data.WeatherData
import ru.mascot.features.weather.api.WeatherApi
import ru.mascot.features.weather.data.WeatherRepository
import ru.mascot.features.weather.data.WeatherSettings
import ru.mascot.features.weather.data.error.ErrorType
import ru.mascot.features.weather.data.error.ResultWrapper
import ru.mascot.features.weather.provider.WeatherProviderFactory
import ru.mascot.features.weather.provider.WeatherProviderFactory.LocationSource
import ru.onyx.map.toPoint

class WeatherModel : ViewModel() {
    private val api: WeatherApi = getKoin().get()

    private val _weather = WeatherRepository.currentWeather
    private val _settings = WeatherRepository.settings
    private val _stats = WeatherRepository.stats

    private var onWeatherUpdate: ((WeatherData) -> Unit)? = null
    private var onLocationUpdate: ((GeoPoint) -> Unit)? = null

    var uiState by mutableStateOf(WeatherUiState())

    init {
        api

        uiState = uiState.copy(
            providers = WeatherProviderFactory.getProviderNames(),
            sources = LocationSource.getSources()
        )

        viewModelScope.launch {
            _weather.collect { data ->
                data.let {
                    val location = uiState.location ?: GeoPoint(data.latitude, data.longitude)

                    uiState = uiState.copy(
                        weather = data,
                        location = location
                    )

                    onLocationUpdate?.invoke(location)
                    onWeatherUpdate?.invoke(data)
                    //onWeatherUpdate = null
                }
            }
        }

        viewModelScope.launch {
            combine(_settings, _stats) { settings, stats ->
                val stat = stats.find { it.provider == settings.provider }
                val provider = WeatherProviderFactory.getProvider(settings.provider)

                val remaining = provider?.getRemaining(provider.perDay, stat?.requests ?: 0L)

                Pair(stat, remaining)
            }.collect { (_, remaining) ->
                uiState = uiState.copy(remaining = remaining)
            }
        }

        viewModelScope.launch {
            _settings.collect {
                uiState = uiState.copy(provider = it.provider, source = it.source)
            }
        }
    }

    fun onMove() {
        viewModelScope.launch {
            uiState.location?.let { location ->
                getKoin().getOrNull<MapState>()
                    ?.scrollTo(location.toPoint().x, location.toPoint().y)
            }
        }
    }

    fun onWeatherUpdate(callback: (WeatherData) -> Unit) {
        onWeatherUpdate = callback
        uiState.weather.let(callback)
    }

    fun updateWeather() {
        uiState.location?.let { location ->
            viewModelScope.launch {
                when (val result = api.fetchWeather(location, _settings.value.provider, true)) {
                    is ResultWrapper.Success -> {
                        updateError(null)
                    }
                    is ResultWrapper.Failure -> {
                        updateError(result.error.message)
                    }
                }
            }
        }
    }

    fun updateHourly(data: HourlyData) {
        uiState = uiState.copy(hourly = data)
    }

    fun onLocationUpdate(callback: (GeoPoint) -> Unit) {
        onLocationUpdate = callback
        uiState.location?.let(callback)
    }

    fun updateLocation(location: GeoPoint) {
        uiState = uiState.copy(location = location)
        onLocationUpdate?.invoke(location)
    }

    fun updateProvider(provider: String) {
        api.updateSettings(WeatherSettings(provider, _settings.value.source))
    }

    fun updateSource(source: String) {
        api.updateSettings(WeatherSettings(_settings.value.provider, source))
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
    val location: GeoPoint? = null,
    val remaining: Long? = null,
    val error: String? = null
)