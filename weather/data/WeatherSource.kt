package ru.mascot.features.weather.data

interface WeatherSource {
    val name: String
    val update: Long
}