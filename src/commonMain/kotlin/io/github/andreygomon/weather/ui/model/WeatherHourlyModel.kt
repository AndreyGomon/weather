package io.github.andreygomon.weather.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.andreygomon.weather.api.WeatherApi
import io.github.andreygomon.weather.data.HourlyData
import io.github.andreygomon.weather.data.HourlyList
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.data.error.ResultWrapper
import io.github.andreygomon.weather.model.Coordinates
import kotlinx.coroutines.launch

class WeatherHourlyModel(
    private val api: WeatherApi,
) : ViewModel() {
    private var hourlyUpdateCallback: ((HourlyData) -> Unit)? = null

    var uiState by mutableStateOf(HourlyUiState())
        private set

    init {
        viewModelScope.launch {
            api.currentHourly.collect { hourly ->
                uiState = uiState.copy(hourly = hourly)
                hourly.list.firstOrNull()?.let { hourlyUpdateCallback?.invoke(it) }
            }
        }
    }

    fun onHourlyUpdate(callback: (HourlyData) -> Unit) {
        hourlyUpdateCallback = callback
        uiState.hourly.list.firstOrNull()?.let(callback)
    }

    fun updateHourly(weatherData: WeatherData) {
        uiState = uiState.copy(isDay = weatherData.isDay())
        val location = uiState.location ?: return

        viewModelScope.launch {
            when (
                val result = api.fetchHourly(
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

    fun updateLocation(location: Coordinates) {
        uiState = uiState.copy(location = location)
    }

    fun updateError(error: String?) {
        uiState = uiState.copy(error = error)
    }
}

data class HourlyUiState(
    val hourly: HourlyList = HourlyList(emptyList()),
    val isDay: Boolean = true,
    val error: String? = null,
    val location: Coordinates? = null,
)
