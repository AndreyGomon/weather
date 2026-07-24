package io.github.andreygomon.weather.provider

import io.github.andreygomon.weather.provider.openmeteo.OpenMeteoProvider

/** Registry of weather providers available to the library. */
object WeatherProviderFactory {
    private val factories = mutableMapOf<String, () -> WeatherProvider>(
        OpenMeteoProvider.NAME to ::OpenMeteoProvider,
    )
    private val instances = mutableMapOf<String, WeatherProvider>()

    enum class LocationSource(val displayName: String) {
        MANUAL("Задать координаты"),
        HOST("Получать из приложения"),
        UNKNOWN("Не выбрано");

        companion object {
            fun findSource(name: String): LocationSource =
                entries.find { it.displayName == name } ?: UNKNOWN

            fun getSources(): List<String> =
                entries.filterNot { it == UNKNOWN }.map { it.displayName }
        }
    }

    fun register(name: String, factory: () -> WeatherProvider) {
        factories[name] = factory
        instances.remove(name)
    }

    fun unregister(name: String) {
        factories.remove(name)
        instances.remove(name)
    }

    fun getProvider(name: String): WeatherProvider? =
        instances[name] ?: factories[name]?.invoke()?.also { instances[name] = it }

    fun createProvider(name: String): WeatherProvider? = factories[name]?.invoke()

    fun getProviderNames(): List<String> = factories.keys.sorted()
}
