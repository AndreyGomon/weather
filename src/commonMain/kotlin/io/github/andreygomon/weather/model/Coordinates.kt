package io.github.andreygomon.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double,
)