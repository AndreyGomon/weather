package io.github.andreygomon.weather

import io.github.andreygomon.weather.data.WeatherCode
import io.github.andreygomon.weather.model.Coordinates
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherModelTest {
    private val json = Json

    @Test
    fun coordinatesKeepValues() {
        val point = Coordinates(latitude = 55.7558, longitude = 37.6173)

        assertEquals(55.7558, point.latitude)
        assertEquals(37.6173, point.longitude)
    }

    @Test
    fun weatherCodeRoundTripsAsNumber() {
        val encoded = json.encodeToString(WeatherCode.RainModerate)
        val decoded = json.decodeFromString<WeatherCode>(encoded)

        assertEquals("63", encoded)
        assertEquals(WeatherCode.RainModerate, decoded)
    }
}
