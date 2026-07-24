package io.github.andreygomon.weather.provider.nominatim

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.github.andreygomon.weather.provider.LocationProvider
import io.github.andreygomon.weather.provider.WeatherClient

class NominatimProvider : LocationProvider {
    companion object {
        const val NAME = "Nominatim"
    }

    override val name: String = NAME
    override val url: String = "https://nominatim.openstreetmap.org/reverse"
    override val perMinute: Long
        get() = NominatimRequests.PER_MINUTE.maxRequests
    override val perHour: Long
        get() = NominatimRequests.PER_HOUR.maxRequests
    override val perDay: Long
        get() = NominatimRequests.PER_DAY.maxRequests

    override suspend fun getLocation(latitude: Double, longitude: Double): String {
        val http = "$url?format=json&lat=$latitude&lon=$longitude"

        val string = WeatherClient.client.get(http) {
            headers.append(HttpHeaders.UserAgent, "AndreyGomonWeather/0.1 (weatherapi@mailfence.com)")
        }.bodyAsText()
        val response = WeatherClient.json.decodeFromString<NominatimResponse>(string)

        return response.address.name
    }
}
