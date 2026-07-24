package io.github.andreygomon.weather.provider

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

internal object WeatherClient {
    val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    val client: HttpClient by lazy {
        createPlatformHttpClient(json)
    }
}

internal expect fun createPlatformHttpClient(json: Json): HttpClient
