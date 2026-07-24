package io.github.andreygomon.weather.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.andreygomon.weather.api.WeatherApi
import io.github.andreygomon.weather.data.ForecastList
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.data.error.ResultWrapper
import io.github.andreygomon.weather.model.Coordinates
import kotlinx.coroutines.launch

class WeatherForecastModel(
    private val api: WeatherApi,
) : ViewModel() {
    var uiState by mutableStateOf(ForecastUiState())
        private set

    init {
        viewModelScope.launch {
            api.currentForecast.collect { forecast ->
                uiState = uiState.copy(forecast = forecast)
            }
        }
    }

    fun updateForecast(weatherData: WeatherData) {
        uiState = uiState.copy(isDay = weatherData.isDay())
        val location = uiState.location ?: return

        viewModelScope.launch {
            when (
                val result = api.fetchForecast(
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

data class ForecastUiState(
    val forecast: ForecastList = ForecastList(emptyList()),
    val location: Coordinates? = null,
    val error: String? = null,
    val isDay: Boolean = false,
)
