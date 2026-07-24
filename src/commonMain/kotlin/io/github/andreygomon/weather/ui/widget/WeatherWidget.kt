package io.github.andreygomon.weather.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.ui.components.LottieAnimation

@Composable
fun WeatherWidget(
    modifier: Modifier = Modifier,
    time: String,
    data: WeatherData,
    gifJson: String,
    compact: Boolean,
    unknown: Boolean
) {
    Widget(
        modifier = modifier,
        locality = data.locality,
        time = time,
        temperature = data.getTemperature,
        felt = data.getFelt,
        description = data.code.message,
        temperatureDay = (data.temperature?.plus(3.0))?.toInt().toString(),
        temperatureNight = (data.temperature?.minus(5.0))?.toInt().toString(),
        windSpeed = data.getWindSpeed,
        windGust = data.getWindGusts10,
        windDirection = data.windDirection10().label,
        humidity = data.getHumidity,
        provider = data.provider,
        gifJson = gifJson,
        compact = compact,
        unknown = unknown
    )
}

@Composable
private fun Widget(
    modifier: Modifier = Modifier,
    locality: String,
    time: String,
    temperature: String,
    felt: String,
    description: String,
    temperatureDay: String,
    temperatureNight: String,
    windSpeed: String,
    windGust: String,
    windDirection: String,
    humidity: String,
    provider: String,
    gifJson: String,
    compact: Boolean,
    unknown: Boolean
) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LottieAnimation(gifJson) { painter ->
                Image(
                    modifier = Modifier
                        .size(if (compact) 40.dp else 128.dp)
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(if (compact) 16.dp else 32.dp)
                        ),
                    painter = painter,
                    contentDescription = ""
                )
            }
            Column(
                modifier = Modifier
            ) {
                if (compact) {
                    Text(
                        text = if (unknown) "?°" else temperature,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Ветер ${if (unknown) "? м/с" else windSpeed}",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Text(
                        modifier = Modifier.basicMarquee(
                            iterations = Int.MAX_VALUE,
                            initialDelayMillis = 2000,
                            repeatDelayMillis = 2000
                        ),
                        text = locality,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = if (unknown) "?°" else temperature,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (!compact) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            maxLines = 2
                        ) {
                            Text(
                                text = if (unknown) "Неизвестно" else description,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = if (unknown) "?°/?°" else "$temperatureDay°/$temperatureNight°",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Ощущается: ${if (unknown) "?°" else felt}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        if (!compact) {
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = "Ощущается: ${if (unknown) "?°" else felt} ",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Скорость ветра: ${if (unknown) "? м/с" else windSpeed}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Порывы ветра: ${if (unknown) "? м/с" else windGust} ",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Направление ветра: ${if (unknown) "Неизвестно" else windDirection}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Влажность: ${if (unknown) "?%" else humidity}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Обновлено: " + if (unknown) "?" else time,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Источник: ${if (unknown) "?" else provider}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}