package io.github.andreygomon.weather.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import io.github.andreygomon.weather.data.HourlyData
import io.github.andreygomon.weather.data.WeatherAnimations.AnimationType
import io.github.andreygomon.weather.data.WeatherAnimations.gif
import io.github.andreygomon.weather.data.WeatherCode
import io.github.andreygomon.weather.ui.model.WeatherHourlyModel
import io.github.andreygomon.weather.ui.components.LottieAnimation

@Composable
fun WeatherHourly(
    modifier: Modifier = Modifier,
    hourlyModel: WeatherHourlyModel = koinViewModel()
) {
    val uiState = hourlyModel.uiState
    val isUnknown = uiState.hourly.list.isEmpty()
    val next24Hours = remember(uiState.hourly) { uiState.hourly.next24Hours() }
    val list = if (isUnknown) listOf(HourlyData()) else next24Hours.hours

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {},
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (!isUnknown && next24Hours.isStale) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    LottieAnimation(
                        gif(
                            AnimationType.SIMPLE,
                            WeatherCode.CodeYellow,
                            true
                        )
                    ) { painter ->
                        Image(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = Color.Black,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            painter = painter,
                            contentDescription = ""
                        )
                    }
                    Text("Данные прогноза устарели")
                }
            }
            Text(
                "Прогноз на 24 часа",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )

            LazyRow(
                userScrollEnabled = true,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(2.dp)
            ) {
                items(
                    items = list,
                    key = { it.timestamp ?: 0L }
                ) { data ->
                    HourlyCard(
                        data,
                        uiState.isDay,
                        isUnknown
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyCard(
    data: HourlyData,
    isDay: Boolean,
    isUnknown: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        LottieAnimation(
            gif(
                AnimationType.SIMPLE,
                data.code,
                isDay
            )
        ) { painter ->
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = if (isUnknown) "?" else data.getTemperature,
                fontSize = 14.sp
            )
            Text(
                text = if (isUnknown) "?" else data.getWindSpeed10,
                maxLines = 1,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = if (isUnknown) "?" else data.getHour,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}