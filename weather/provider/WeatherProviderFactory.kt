package ru.mascot.features.weather.provider

import ru.mascot.features.weather.provider.openMeteo.OpenMeteoProvider
import ru.mascot.features.weather.provider.yandex.Yandex

object WeatherProviderFactory {
    private val _provider: Map<String, () -> WeatherProvider> = mapOf(
        OpenMeteoProvider.NAME to { OpenMeteoProvider() },
        //Yandex.NAME to { Yandex() }
    )

    private val _providers: MutableMap<String, WeatherProvider> = mutableMapOf()

    enum class FixedTimeoitType(val displayName: String, val value: Long) {
        QUAD("15 минут", 900_000L),
        HALF("30 минут", 1_800_000L),
        HOUR("1 час", 3_600_000L),
        THREE("3 часа", 10_800_000L),
        SIX("6 часов", 21_600_000L),
        TWELVE("12 часов", 43_200_000L),
        UNKNOWN("Не выбрано", 0L);

        companion object {
            fun getMillis(type: String): Long? {
                val timeoutType = findTimeout(type)
                return if (timeoutType != UNKNOWN) timeoutType.value else null
            }

            fun getFixedMillis(type: String): Long {
                return QUAD.value
            }

            fun findTimeout(name: String): FixedTimeoitType =
                entries.find { it.displayName == name } ?: UNKNOWN

            fun getTimeouts(): List<String> =
                entries.filter { it != UNKNOWN }.map { it.displayName }
        }
    }

    enum class TimeoutType(val displayName: String, val value: Long) {
        SECONDS("Секунды", 1_000L),
        MINUTES("Минуты", 60_000L),
        HOURS("Часы", 3_600_000L),
        UNKNOWN("Не выбрано", 0L);

        companion object {
            fun getMillis(type: String, count: Long): Long? {
                val timeoutType = findTimeout(type)
                return if (timeoutType != UNKNOWN) count * timeoutType.value else null
            }

            fun findTimeout(name: String): TimeoutType =
                entries.find { it.displayName == name } ?: UNKNOWN

            fun getTimeouts(): List<String> =
                entries.filter { it != UNKNOWN }.map { it.displayName }
        }
    }

    enum class LocationSource(val displayName: String) {
        CENTER("Центральная точка"),
        MANUAL("Задать координаты"),
        MAP("Указать на карте"),
        LOCATION("По геопозиции"),
        UNKNOWN("Не выбрано");

        companion object {
            fun findSource(name: String): LocationSource =
                entries.find { it.displayName == name } ?: UNKNOWN

            fun getSources(): List<String> = entries.filter { it != UNKNOWN && it != LOCATION && it != MAP }.map { it.displayName }
        }
    }

    fun getProvider(name: String): WeatherProvider? {
        return _providers.getOrPut(name) {
            _provider[name]?.invoke() ?: return null
        }
    }

    fun createProvider(name: String): WeatherProvider? {
        return _provider[name]?.invoke()
    }

    fun getProviderNames(): List<String> {
        return _provider.keys.toList()
    }
}