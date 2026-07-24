package io.github.andreygomon.weather.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import io.github.andreygomon.weather.data.ForecastData
import io.github.andreygomon.weather.data.WeatherAnimations.AnimationType
import io.github.andreygomon.weather.data.WeatherAnimations.gif
import io.github.andreygomon.weather.data.WeatherCode
import io.github.andreygomon.weather.ui.model.WeatherForecastModel
import io.github.andreygomon.weather.ui.components.LottieAnimation
import io.github.andreygomon.weather.ui.icons.ChevronDown
import io.github.andreygomon.weather.ui.icons.WeatherIcons

@Composable
fun WeatherForecast(
    modifier: Modifier = Modifier,
    forecastModel: WeatherForecastModel = koinViewModel()
) {
    val uiState = forecastModel.uiState
    val isUnknown = uiState.forecast.list.isEmpty()

    var expand by remember { mutableStateOf(false) }
    val list = remember(uiState.forecast, expand) {
        when {
            isUnknown -> listOf(ForecastData())
            expand -> uiState.forecast.list
            else -> uiState.forecast.list.take(1)
        }
    }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                expand = !expand
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Error(message = uiState.error)
        Text("Прогноз на неделю", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Column(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            list.forEachIndexed { index, data ->
                key(data.timestamp) {
                    DayCard(
                        data = list[index],
                        isDay = uiState.isDay,
                        isUnknown = isUnknown
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!expand) {
                    Icon(
                        imageVector = WeatherIcons.ChevronDown,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun DayCard(
    data: ForecastData,
    isDay: Boolean,
    isUnknown: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.widthIn(min = 48.dp, max = 56.dp)
        ) {
            Text(
                text = if (isUnknown) "Неизвестно" else data.getDayWeek(true),
                lineHeight = 14.sp,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (isUnknown) "Неизвестно" else data.getDateMonth(true),
                lineHeight = 10.sp,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(48.dp)
                .widthIn(max = 48.dp)
                .padding(start = 2.dp, end = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(gif(AnimationType.SIMPLE, data.code, isDay)) { painter ->
                Image(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    painter = painter,
                    contentDescription = ""
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.widthIn(min = 56.dp)
        ) {
            Text(
                if (isUnknown) "?" else data.getTemperatureMax,
                fontSize = 18.sp,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                if (isUnknown) "?" else data.getTemperatureMin,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.widthIn(min = 72.dp, max = 72.dp)
        ) {
            Box(
                modifier = Modifier.size(32.dp).widthIn(32.dp),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(gif(AnimationType.SIMPLE, WeatherCode.Humidity, isDay)) { painter ->
                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painter,
                        contentDescription = ""
                    )
                }
            }
            Text(
                modifier = Modifier.widthIn(min = 48.dp),
                text = if (isUnknown) "Неизвестно" else data.getPrecipitation,
                maxLines = 1,
                fontSize = 14.sp,
                color = Color(0xFF0077CC)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.widthIn(min = 80.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(32.dp)
                        .size(32.dp, 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(gif(AnimationType.SIMPLE, WeatherCode.Wind, isDay)) { painter ->
                        Image(
                            modifier = Modifier.size(48.dp),
                            painter = painter,
                            contentDescription = ""
                        )
                    }
                }
                Text(
                    modifier = Modifier.widthIn(72.dp),
                    text = if (isUnknown) "Неизвестно" else data.getWindSpeed,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
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
