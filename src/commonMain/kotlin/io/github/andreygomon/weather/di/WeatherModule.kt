package io.github.andreygomon.weather.di

import app.cash.sqldelight.db.SqlDriver
import io.github.andreygomon.weather.api.WeatherApi
import io.github.andreygomon.weather.api.WeatherApiImpl
import io.github.andreygomon.weather.data.storage.SqlDelightWeatherStorage
import io.github.andreygomon.weather.data.storage.WeatherStorage
import io.github.andreygomon.weather.service.WeatherService
import io.github.andreygomon.weather.ui.model.WeatherForecastModel
import io.github.andreygomon.weather.ui.model.WeatherHourlyModel
import io.github.andreygomon.weather.ui.model.WeatherModel
import io.github.andreygomon.weather.ui.model.WeatherSettingsModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/** Koin module for applications that want the optional ready-made integration. */
fun weatherModule(driver: SqlDriver): Module = module {
    single<WeatherStorage> { SqlDelightWeatherStorage(driver) }
    single<WeatherApi> { WeatherApiImpl(get()) }
    single { WeatherService(get()) }

    viewModelOf(::WeatherModel)
    viewModelOf(::WeatherForecastModel)
    viewModelOf(::WeatherHourlyModel)
    viewModelOf(::WeatherSettingsModel)
}
