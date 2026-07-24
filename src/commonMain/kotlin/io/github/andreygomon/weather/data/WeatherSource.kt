package io.github.andreygomon.weather.data

interface WeatherSource {
    val name: String
    val update: Long
}