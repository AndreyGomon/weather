package io.github.andreygomon.weather.ui.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import io.github.andreygomon.weather.data.WeatherAnimations.AnimationType
import io.github.andreygomon.weather.data.WeatherAnimations.gif
import io.github.andreygomon.weather.data.WeatherCode
import io.github.andreygomon.weather.model.Coordinates
import io.github.andreygomon.weather.util.DateDay
import io.github.andreygomon.weather.provider.WeatherProviderFactory.LocationSource
import io.github.andreygomon.weather.ui.components.Dropdown
import io.github.andreygomon.weather.ui.icons.WeatherIcons
import io.github.andreygomon.weather.ui.icons.Restart
import io.github.andreygomon.weather.ui.icons.InfoDanger
import io.github.andreygomon.weather.ui.icons.ShareSquare
import io.github.andreygomon.weather.ui.icons.Pen
import io.github.andreygomon.weather.ui.model.WeatherForecastModel
import io.github.andreygomon.weather.ui.model.WeatherHourlyModel
import io.github.andreygomon.weather.ui.model.WeatherModel
import io.github.andreygomon.weather.ui.widget.WeatherWidget
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * Standalone weather screen.
 *
 * Map and geolocation integrations belong to the host application. Supply a location through
 * [initialLocation] or call [WeatherModel.updateLocation] from the host.
 */
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    initialLocation: Coordinates? = null,
    onMoveToLocation: ((Coordinates) -> Unit)? = null,
    weatherModel: WeatherModel = koinViewModel(),
    forecastModel: WeatherForecastModel = koinViewModel(),
    hourlyModel: WeatherHourlyModel = koinViewModel(),
) {
    val uiState = weatherModel.uiState
    var coordinatesDialogVisible by remember { mutableStateOf(false) }

    val isUnknown by remember(uiState.weather) {
        derivedStateOf { uiState.weather.code == WeatherCode.Unknown }
    }
    val flightForbidden by remember(uiState.weather) {
        derivedStateOf {
            val humidity = uiState.weather.humidity ?: return@derivedStateOf false
            val windSpeed = uiState.weather.windSpeed ?: return@derivedStateOf false
            val temperature = uiState.weather.temperature ?: return@derivedStateOf false

            humidity >= 80 ||
                windSpeed >= 10 ||
                temperature < -10 ||
                temperature > 35 ||
                (temperature in -5.0..3.0 && humidity > 75)
        }
    }

    val updatedAt = remember(uiState.weather.update) {
        uiState.weather.update
            .takeIf { it > 0 }
            ?.let { DateDay.formatHour(it) }
            ?: "?"
    }
    val animation = gif(
        AnimationType.SIMPLE,
        uiState.weather.code,
        uiState.weather.isDay(),
    )

    LaunchedEffect(initialLocation) {
        initialLocation?.let { weatherModel.updateLocation(it) }
    }

    LaunchedEffect(weatherModel, hourlyModel, forecastModel) {
        hourlyModel.onHourlyUpdate(weatherModel::updateHourly)
        weatherModel.onLocationUpdate { point ->
            hourlyModel.updateLocation(point)
            forecastModel.updateLocation(point)
        }
        weatherModel.onWeatherUpdate { data ->
            forecastModel.updateForecast(data)
            hourlyModel.updateHourly(data)
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Provider(
                provider = uiState.provider,
                providers = uiState.providers,
                onValueChanged = weatherModel::updateProvider,
            )

            uiState.remaining?.let { Text("Запросов осталось: $it") }

            Source(
                source = uiState.source,
                sources = uiState.sources,
                location = uiState.location,
                onValueChanged = weatherModel::updateSource,
                onEditLocation = { coordinatesDialogVisible = true },
                onMoveToLocation = {
                    uiState.location?.let { onMoveToLocation?.invoke(it) }
                },
            )

            Error(uiState.error)

            Row(verticalAlignment = Alignment.CenterVertically) {
                WeatherWidget(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    time = updatedAt,
                    data = uiState.weather,
                    gifJson = animation,
                    compact = false,
                    unknown = isUnknown,
                )
                SourceButton(
                    animation = { angle, _ ->
                        angle.animateTo(360f, tween(500))
                        angle.snapTo(0f)
                    },
                    imageVector = WeatherIcons.Restart,
                    onClick = weatherModel::updateWeather,
                )
            }

            WeatherDetails(uiState.weather, isUnknown)
            WeatherHourly(hourlyModel = hourlyModel)
            WeatherForecast(forecastModel = forecastModel)

            AnimatedVisibility(flightForbidden) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = WeatherIcons.InfoDanger,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(40.dp),
                        )
                        Column {
                            Text("Полёты не рекомендуются", style = MaterialTheme.typography.titleMedium)
                            Text("Текущие погодные условия могут быть опасными")
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }

    if (coordinatesDialogVisible) {
        CoordinatesDialog(
            initial = uiState.location,
            onDismiss = { coordinatesDialogVisible = false },
            onApply = { point ->
                weatherModel.updateLocation(point, refresh = true)
                coordinatesDialogVisible = false
            },
        )
    }
}

@Composable
fun Provider(
    provider: String,
    providers: List<String>,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Dropdown(
        value = provider,
        values = providers,
        label = "Источник",
        onValueChanged = onValueChanged,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun Source(
    source: String,
    sources: List<String>,
    location: Coordinates?,
    onValueChanged: (String) -> Unit,
    onEditLocation: () -> Unit,
    onMoveToLocation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedSource = remember(source) { LocationSource.findSource(source) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Dropdown(
            value = source,
            values = sources,
            label = "Координаты",
            onValueChanged = onValueChanged,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = location?.let { "Ш: ${it.latitude}  Д: ${it.longitude}" }
                    ?: "Координаты не заданы",
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            if (selectedSource == LocationSource.MANUAL) {
                IconButton(onClick = onEditLocation) {
                    Icon(WeatherIcons.Pen, contentDescription = "Изменить координаты")
                }
            }
            if (location != null) {
                IconButton(onClick = onMoveToLocation) {
                    Icon(WeatherIcons.ShareSquare, contentDescription = "Показать координаты")
                }
            }
        }
    }
}

@Composable
fun SourceButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    animation: (suspend (Animatable<Float, AnimationVector1D>, Animatable<Float, AnimationVector1D>) -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    val angle = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    IconButton(
        modifier = modifier.graphicsLayer {
            rotationZ = angle.value
            scaleX = scale.value
            scaleY = scale.value
        },
        onClick = {
            scope.launch { animation?.invoke(angle, scale) }
            onClick()
        },
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
    }
}

@Composable
private fun CoordinatesDialog(
    initial: Coordinates?,
    onDismiss: () -> Unit,
    onApply: (Coordinates) -> Unit,
) {
    var latitude by remember(initial) { mutableStateOf(initial?.latitude?.toString().orEmpty()) }
    var longitude by remember(initial) { mutableStateOf(initial?.longitude?.toString().orEmpty()) }
    val point = remember(latitude, longitude) {
        val lat = latitude.replace(',', '.').toDoubleOrNull()
        val lon = longitude.replace(',', '.').toDoubleOrNull()
        if (lat != null && lon != null && lat in -90.0..90.0 && lon in -180.0..180.0) {
            Coordinates(lat, lon)
        } else {
            null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Координаты") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Широта") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Долгота") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            Button(enabled = point != null, onClick = { point?.let(onApply) }) {
                Text("Применить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
    )
}

@Composable
private fun Error(message: String?) {
    AnimatedVisibility(message != null) {
        Text(
            text = message.orEmpty(),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
        )
    }
}
