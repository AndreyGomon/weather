package ru.mascot.features.weather.plugin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.compose.getKoin
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.oscim.core.GeoPoint
import ru.mascot.features.objects.presentation.contract.PayloadDrawer
import ru.mascot.features.objects.presentation.drawtypes.DrawComposable
import ru.mascot.features.objects.presentation.drawtypes.DrawType
import ru.mascot.features.weather.api.WeatherApiImpl
import ru.mascot.features.weather.data.WeatherAnimations
import ru.mascot.features.weather.data.WeatherAnimations.gif
import ru.mascot.features.weather.data.WeatherCode
import ru.mascot.features.weather.data.error.ErrorType
import ru.mascot.features.weather.data.error.ResultWrapper
import ru.mascot.features.weather.ui.widget.WeatherWidget
import ru.mascot.features.weather.utils.Timer
import ru.mascot.utils.formatter.DateFormat
import ru.onyx.map.toPoint
import kotlin.time.Instant

@Single(binds = [PayloadDrawer::class])
@Named(type = WeatherType::class)
internal class WeatherDrawer : PayloadDrawer<WeatherPayload> {
    override fun draw(obj: WeatherPayload): List<DrawType> {
        val point = obj.geoPoint ?: return emptyList()
        return listOf(
            DrawComposable(point = point.toPoint()) {
                WeatherDraw(payload = obj, point = point)
            }
        )
    }
}

@Composable
private fun WeatherDraw(
    payload: WeatherPayload,
    point: GeoPoint
) {
    val weatherData = remember(payload.geoPoint, payload.provider) {
        mutableStateOf(payload.data.copy(provider = payload.provider))
    }

    val api = getKoin().get<WeatherApiImpl>()
    val time = DateFormat.watchRelativeTime(Instant.fromEpochMilliseconds(weatherData.value.update ?: 0))
    val isUnknown by remember(weatherData.value.code) {
        derivedStateOf { weatherData.value.code == WeatherCode.Unknown }
    }

    val scope = rememberCoroutineScope()

    DisposableEffect(payload.geoPoint, payload.provider) {
        val job = Timer(scope = scope, Dispatchers.IO).launch(
            key = "map:${point.latitude},${point.longitude}",
            period = WeatherApiImpl.CURRENT_TTL.inWholeMilliseconds,
            jitterMinMs = 1_000L,
            jitterMaxMs = 60_000L,
            runImmediately = payload.isInitialized
        ) {
            when (val result = api.fetchWeather(point, weatherData.value.provider, current = false)) {
                is ResultWrapper.Success -> {
                    weatherData.value = result.value
                }

                is ResultWrapper.Failure -> {
                    println(result.error.message) //TODO возможно требуется добавление логики виджета
                }
            }
        }
        onDispose {
            job.cancel()
        }
    }

    WeatherWidget(
        time = time.value,
        data = weatherData.value,
        gifJson = gif(
            WeatherAnimations.AnimationType.SIMPLE,
            weatherData.value.code,
            weatherData.value.isDay()
        ),
        compact = true,
        unknown = isUnknown
    )
}