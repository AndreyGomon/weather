package io.github.andreygomon.weather.provider

import io.github.andreygomon.weather.provider.nominatim.NominatimProvider

object LocationProviderFactory {
    private val _provider: Map<String, () -> LocationProvider> = mapOf(
        NominatimProvider.NAME to { NominatimProvider() },
    )

    private val _providers: MutableMap<String, LocationProvider> = mutableMapOf()

    fun getProvider(name: String): LocationProvider? {
        return _providers.getOrPut(name) {
            _provider[name]?.invoke() ?: return null
        }
    }

    fun createProvider(name: String): LocationProvider? {
        return _provider[name]?.invoke()
    }

    fun getProviderNames(): List<String> {
        return _provider.keys.toList()
    }
}