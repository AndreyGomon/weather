package ru.mascot.features.weather.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import org.oscim.core.GeoPoint
import ru.mascot.features.weather.data.ForecastList
import ru.mascot.features.weather.data.WeatherData
import ru.mascot.features.weather.api.WeatherApi
import ru.mascot.features.weather.data.WeatherRepository
import ru.mascot.features.weather.data.error.ResultWrapper

class WatherForecastModel : ViewModel() {
    private val api: WeatherApi = getKoin().get()

    private val _forecast = WeatherRepository.currentForecast
    private val _settings =  WeatherRepository.settings

    var uiState by mutableStateOf(ForecastUiState())
        private set

    init {
        viewModelScope.launch {
            _forecast.collect { forecast ->
                uiState = uiState.copy(forecast = forecast)
            }
        }
    }

    fun updateForecast(weatherData: WeatherData) {
        uiState = uiState.copy(isDay = weatherData.isDay())

        uiState.location?.let { location ->
            viewModelScope.launch {
                when (val result = api.fetchForecast(location, _settings.value.provider, true)) {
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

    fun updateIcon(string: String) {

    }
}



data class ForecastUiState(
    val forecast: ForecastList = ForecastList(listOf()),
    val location: GeoPoint? = null,
    val error: String? = null,
    val isDay: Boolean = false
)