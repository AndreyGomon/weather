package ru.mascot.features.weather.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import org.oscim.core.GeoPoint
import ovh.plrapps.mapcompose.api.centroidX
import ovh.plrapps.mapcompose.api.centroidY
import ovh.plrapps.mapcompose.ui.state.MapState
import ovh.plrapps.mapcompose.utils.Point
//import ru.mascot.features.map.utils.EnterCoordinatesDialog
import ru.mascot.features.weather.data.WeatherAnimations.AnimationType
import ru.mascot.features.weather.data.WeatherAnimations.gif
import ru.mascot.features.weather.data.WeatherCode
import ru.mascot.features.weather.provider.WeatherProviderFactory.LocationSource
import ru.mascot.features.weather.ui.components.Dropdown
import ru.mascot.features.weather.ui.model.WatherForecastModel
import ru.mascot.features.weather.ui.model.WeatherHourlyModel
import ru.mascot.features.weather.ui.model.WeatherModel
import ru.mascot.features.weather.ui.widget.WeatherWidget
import ru.mascot.utils.formatter.DateFormat
import ru.mascot.utils.ui.MascotListItem
import ru.mascot.utils.ui.MascotPrimaryIconButton
import ru.mascot.utils.ui.icons.Crosshairs
import ru.mascot.utils.ui.icons.InfoDanger
import ru.mascot.utils.ui.icons.MascotIcons
import ru.mascot.utils.ui.icons.Pen
import ru.mascot.utils.ui.icons.Restart
import ru.mascot.utils.ui.icons.ShareSquare
import ru.onyx.map.converter.CoordinatePoint
import ru.onyx.map.converter.toCoordinatePoint
import ru.onyx.map.converter.toGeoPoint
import ru.onyx.map.coordinates.presentation.EnterCoordinatesDialog
import ru.onyx.map.coordinates.presentation.rememberEnterModel
import ru.onyx.map.leftClicked
import ru.onyx.map.toGeoPoint

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    weatherModel: WeatherModel = viewModel(),
    forecastModel: WatherForecastModel = viewModel(),
    hourlyModel: WeatherHourlyModel = viewModel(),
    mapState: MapState? = getKoin().getOrNull<MapState>()
) {
    val uiState by weatherModel::uiState

    var coordinate by remember { mutableStateOf<CoordinatePoint?>(null) }
    var coordinatesDialog by remember { mutableStateOf(false) }
    var coordinatesMouse by remember { mutableStateOf(false) }

    val isUnknown by remember(uiState.weather) {
        derivedStateOf { uiState.weather.code == WeatherCode.Unknown }
    }

    val time = DateFormat.watchRelativeTime(
        kotlin.time.Instant.fromEpochMilliseconds(
            uiState.weather.update ?: 0
        )
    )

    val flightForbidden by remember(uiState.weather) {
        derivedStateOf {
            val humidity = uiState.weather.humidity ?: return@derivedStateOf false
            val windSpeed = uiState.weather.windSpeed ?: return@derivedStateOf false
            val temp = uiState.weather.temperature ?: return@derivedStateOf false

            val highHumidity = humidity >= 80
            val highWind = windSpeed >= 10
            val outOfTempRange = temp < -10 || temp > 35
            val riskyTempAndHumidity = temp in -5.0..3.0 && humidity > 75

            highHumidity || highWind || outOfTempRange || riskyTempAndHumidity
        }
    }

    val gifWeather = gif(AnimationType.SIMPLE, uiState.weather.code, uiState.weather.isDay())

    LaunchedEffect(Unit) {
        hourlyModel.onHourlyUpdate { hourly ->
            weatherModel.updateHourly(hourly)
        }

        weatherModel.onLocationUpdate { point ->
            hourlyModel.updateLocation(point)
            forecastModel.updateLocation(point)
        }

        weatherModel.onWeatherUpdate { data ->
            forecastModel.updateForecast(data)
            hourlyModel.updateHourly(data)
        }
    }

    Box {
        Column(
            modifier = modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Provider(
                modifier = modifier,
                provider = uiState.provider,
                providers = uiState.providers,
                onValueChanged = weatherModel::updateProvider
            )

            uiState.remaining?.let {
                Text(text = "Запросов осталось: $it")
            }

            Source(
                modifier,
                uiState.source,
                uiState.sources,
                uiState.location,
                weatherModel::updateSource,
                weatherModel::onMove
            ) {
                when (LocationSource.findSource(weatherModel.uiState.source)) {
                    LocationSource.MANUAL -> {
                        coordinatesDialog = true
                    }

                    LocationSource.MAP -> {
                        coordinatesMouse = true
                    }

                    LocationSource.CENTER -> {
                        mapState?.let { state ->
                            val point = Point(state.centroidX, state.centroidY).toGeoPoint()
                            weatherModel.updateLocation(point)
                        }
                    }

                    else -> {}
                }
            }


            Error(message = uiState.error)

            Row {
                WeatherWidget(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    time = time.value,
                    data = uiState.weather,
                    gifJson = gifWeather,
                    compact = false,
                    unknown = isUnknown
                )

                SourceButton(
                    animation = { angle, _ ->
                        angle.animateTo(360f, tween(500))
                        angle.snapTo(0f)
                    },
                    imageVector = MascotIcons.Restart
                ) {
                    weatherModel.updateWeather()
                }
            }
            WeatherDetails(uiState.weather, isUnknown)

            WeatherHourly(hourlyModel = hourlyModel)
            WeatherForecast(forecastModel = forecastModel)

            if (flightForbidden) {
                Spacer(Modifier.height(96.dp))
            }

            // В coordinate лежит точка в какой-то системе координат.
            // При выводе нужно использовать текущую систему [coordinate.toCurrentSystem()]
            // Для работы сервиса можно использовать [coordinate.toWgs84().toGeoPoint()]
            // Text(text = "Координаты: ${coordinate?.toCurrentSystem().toString()}")
        }

        if (flightForbidden) {
            AnimatedVisibility(
                visible = true,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()

            ) {
                MascotListItem(
                    title = { Text("Запрещено") },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            imageVector = MascotIcons.InfoDanger,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                        )
                    },
                    trailingIcon = null,
                    description = { Text("Выполнять полеты при таких погодных условиях") }

                )
            }
        }
    }

    if (coordinatesDialog) {
        EnterCoordinatesDialog(
            title = { Text(text = "Выбор координат для определения погоды") },
            onApply = { point ->
                weatherModel.updateLocation(point.toGeoPoint())
                weatherModel.updateWeather()
                weatherModel.onMove()
                coordinatesDialog = false
            },
            model = rememberEnterModel(
                initialPoint = uiState.location?.toCoordinatePoint()
            ),
            onDismissRequest = {
                coordinatesDialog = false
            }
        )
    }

    LaunchedEffect(coordinatesMouse) {
        if (coordinatesMouse) {
            mapState?.leftClicked?.collect { point ->
                weatherModel.updateLocation(point.toGeoPoint())
                coordinatesMouse = false
            }
        }
    }
}

@Composable
fun Provider(
    modifier: Modifier = Modifier,
    provider: String,
    providers: List<String>,
    onValueChanged: (String) -> Unit
) {
    Dropdown(
        modifier,
        provider,
        providers,
        "Источник",
        onValueChanged
    )
}

@Composable
fun Source(
    modifier: Modifier = Modifier,
    source: String,
    sources: List<String>,
    location: GeoPoint?,
    onValueChanged: (String) -> Unit,
    onMove: () -> Unit = {},
    onUpdateLocation: () -> Unit
) {
    val findSource = remember(source) { LocationSource.findSource(source) }
    val sourceAttach =
        findSource in listOf(LocationSource.CENTER, LocationSource.MANUAL, LocationSource.MAP)

    Column(
        modifier = modifier
    ) {
        Dropdown(
            value = source,
            values = sources,
            label = "Координаты",
            onValueChanged = onValueChanged
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = location?.run { "Ш: $latitude Д: $longitude" } ?: "Ш:0.0  Д:0.0",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )

            if (findSource in listOf(
                    LocationSource.CENTER,
                    LocationSource.MANUAL,
                    LocationSource.MAP
                )
            ) {
                AnimatedVisibility(visible = sourceAttach) {
                    SourceButton(
                        animation = { angle, _ ->
                            angle.animateTo(45f, tween(100))
                            angle.animateTo(-45f, tween(100))
                            angle.animateTo(0f, tween(100))
                        },
                        imageVector = if (findSource == LocationSource.CENTER) MascotIcons.Crosshairs else MascotIcons.Pen
                    ) {
                        onUpdateLocation()
                    }
                }
            }

            SourceButton(
                animation = { _, scale ->
                    scale.animateTo(1.5f, tween(100))
                    scale.animateTo(1f, tween(100))
                },
                imageVector = MascotIcons.ShareSquare
            ) { onMove() }
        }
    }
}

@Composable
fun SourceButton(
    modifier: Modifier = Modifier,
    animation: (
    suspend (
        angle: Animatable<Float, AnimationVector1D>,
        scale: Animatable<Float, AnimationVector1D>
    ) -> Unit)? = null,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val angle = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    MascotPrimaryIconButton(
        modifier = modifier
            .wrapContentSize()
            .padding(4.dp),
        onClick = {
            scope.launch {
                animation?.invoke(angle, scale)
            }
            onClick()
        }
    ) {
        Icon(
            modifier = Modifier
                .size(56.dp)
                .graphicsLayer {
                    rotationZ = angle.value
                    scaleX = scale.value
                    scaleY = scale.value
                },
            imageVector = imageVector,
            contentDescription = null
        )
    }
}

@Composable
private fun Error(
    message: String?
) {
    val visible = message != null

    AnimatedVisibility(visible = visible) {
        Text(
            text = message.orEmpty(),
            modifier = Modifier
                .graphicsLayer { alpha = if (visible) 1f else 0f }
                .wrapContentSize()
                .then(Modifier),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2
        )
    }
}

val WeatherScreenPreview: @Composable () -> Unit = {
    WeatherScreen()
}