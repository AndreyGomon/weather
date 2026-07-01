package ru.mascot.features.weather.provider.nomenatim

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import ru.mascot.features.weather.provider.LocationProvider
import ru.mascot.features.weather.provider.WeatherClient

class NominatimProvider : LocationProvider {
    companion object {
        const val NAME = "Nomenatim"
    }

    override val name: String = NAME
    override val url: String = "https://nominatim.openstreetmap.org/reverse"
    override val perMinute: Long
        get() = NomenatimRequests.PER_MINUTE.maxRequests
    override val perHour: Long
        get() = NomenatimRequests.PER_HOUR.maxRequests
    override val perDay: Long
        get() = NomenatimRequests.PER_DAY.maxRequests

    override suspend fun getLocation(latitude: Double, longitude: Double): String {
        val http = "$url?format=json&lat=$latitude&lon=$longitude"

        val string = WeatherClient.client.get(http) {
            headers.append("WeatherApi", "MascotWeatherApi/1.0 (weatherapi.mascot@mailfence.com)")
        }.bodyAsText()
        val response = WeatherClient.json.decodeFromString<NominatimResponse>(string)

        return response.address.name
    }
}
