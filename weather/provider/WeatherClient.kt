package ru.mascot.features.weather.provider

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json

object WeatherClient {
    val json: Json by lazy {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
    }

    val client: HttpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
}
