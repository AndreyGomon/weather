package ru.mascot.features.weather.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import org.oscim.core.GeoPoint
import ru.mascot.features.weather.data.HourlyData
import ru.mascot.features.weather.data.HourlyList
import ru.mascot.features.weather.data.WeatherData
import ru.mascot.features.weather.data.WeatherRepository
import ru.mascot.features.weather.api.WeatherApi
import ru.mascot.features.weather.data.error.ErrorType
import ru.mascot.features.weather.data.error.ResultWrapper

class WeatherHourlyModel : ViewModel() {
    private val api: WeatherApi = getKoin().get()
    private val _hourly = WeatherRepository.currentHourly
    private val _settings = WeatherRepository.settings

    private var hourlyUpdateCallback: ((HourlyData) -> Unit)? = null

    var uiState by mutableStateOf(HourlyUiState())
        private set

    init {
        viewModelScope.launch {
            _hourly.collect { hourly ->
                uiState = uiState.copy(hourly = hourly)

                hourlyUpdateCallback?.let { callback ->
                    hourly.list.firstOrNull()?.let(callback)
                }
            }
        }
    }

    fun onHourlyUpdate(callback: (HourlyData) -> Unit) {
        hourlyUpdateCallback = callback
        uiState.hourly.list.firstOrNull()?.let(callback)
    }

    fun updateHourly(weatherData: WeatherData) {
        uiState = uiState.copy(isDay = weatherData.isDay())

        uiState.location?.let { location ->
            viewModelScope.launch {
                when (val result = api.fetchHourly(location, _settings.value.provider, true)) {
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

    fun updateLocation(location: GeoPoint) {
        uiState = uiState.copy(location = location)
    }

    fun updateError(error: String?) {
        uiState = uiState.copy(error = error)
    }
}

data class HourlyUiState(
    val hourly: HourlyList = HourlyList(listOf()),
    val isDay: Boolean = true,
    val error: String? = null,
    val location: GeoPoint? = null,
)