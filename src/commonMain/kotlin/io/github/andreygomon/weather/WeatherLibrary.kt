package io.github.andreygomon.weather

import app.cash.sqldelight.db.SqlDriver
import io.github.andreygomon.weather.api.WeatherApi
import io.github.andreygomon.weather.api.WeatherApiImpl
import io.github.andreygomon.weather.data.storage.SqlDelightWeatherStorage

/**
 * Creates a weather API over a caller-owned SQLDelight driver.
 *
 * The application is responsible for creating the driver and applying
 * [io.github.andreygomon.weather.database.WeatherDatabase.Schema].
 */
fun createWeatherApi(driver: SqlDriver): WeatherApi =
    WeatherApiImpl(SqlDelightWeatherStorage(driver))
