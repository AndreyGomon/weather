package io.github.andreygomon.weather.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.andreygomon.weather.data.WeatherAnimations.AnimationType
import io.github.andreygomon.weather.data.WeatherAnimations.gif
import io.github.andreygomon.weather.data.WeatherCode
import io.github.andreygomon.weather.data.WeatherData
import io.github.andreygomon.weather.ui.components.LottieAnimation

@Composable
fun WeatherDetails(
    data: WeatherData,
    unknown: Boolean
) {
    val expanded = remember { mutableStateOf(false) }

    TextButton(onClick = { expanded.value = !expanded.value }) {
        Text(
            text = if (expanded.value) "Скрыть детали" else "Показать детали",
            style = MaterialTheme.typography.bodySmall
        )
    }

    if (expanded.value) {
        Details(data = data, unknown = unknown)
    }
}

@Composable
private fun Details(
    data: WeatherData,
    unknown: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Header(WeatherCode.Height, "Профиль по высоте", 2f)
        Text(
            "10 м: ${if (unknown) "?" else "${data.getTemperature}, ${data.getWindSpeed}, ${data.windDirection10().label}"}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            "80 м: ${if (unknown) "?" else "${data.getTemperature80}, ${data.getWindSpeed80}, ${data.windDirection80().label}"}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            "120 м: ${if (unknown) "?" else "${data.getTemperature120}, ${data.getWindSpeed120}, ${data.windDirection120().label}"}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            "180 м: ${if (unknown) "?" else "${data.getTemperature180}, ${data.getWindSpeed180}, ${data.windDirection180().label}"}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(6.dp))

        Header(WeatherCode.CloudsHeight, "Облака и видимость", 1f)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Облачность: ${if (unknown) "?" else data.getCloud}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Нижн.: ${if (unknown) "?" else data.getCloudLow}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Средн.: ${if (unknown) "?" else data.getCloudMid}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Верхн.: ${if (unknown) "?" else data.getCloudHigh}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Видимость: ${if (unknown) "?" else data.getVisibility}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Header(WeatherCode.Pressure, "Давление", 1f)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "MSL: ${if (unknown) "?" else data.getSeaPressure}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Поверхн.: ${if (unknown) "?" else data.getPressure}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Header(WeatherCode.Precipitation, "Осадки", 1f)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Дождь(в мм): ${if (unknown) "?" else data.rainPrecipitation}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Ливень(в мм): ${if (unknown) "?" else data.showerPrecipitation}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Снег(в мм): ${if (unknown) "?" else data.snowPrecipitation}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Снежный покров: ${if (unknown) "?" else data.snowCover}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Инт.: ${if (unknown) "?" else data.getPrecipitation}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Вероятн.: ${if (unknown) "?" else data.getPrecipitationProbability}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Тчк росы: ${if (unknown) "?" else data.getDewPoint}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Влажн.: ${if (unknown) "?" else data.getHumidity}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Header(WeatherCode.UVIndex, "Солнечная активность", 1f)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "SW: ${data.shortwaveRadiation ?: "?"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Direct: ${data.directRadiation ?: "?"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "DNI: ${data.directNormalIrradiance ?: "?"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "GTI: ${data.globalTiltedIrradiance ?: "?"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "GTI inst: ${data.globalTiltedIrradianceInstant ?: "?"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Diffuse: ${data.diffuseRadiation ?: "?"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Sunshine: ${data.sunshineDuration ?: "?"} c",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Header(WeatherCode.Soil, "Почва: температура", 1f)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "0 см: ${data.soilTemperature0cm?.toInt() ?: "?"}°",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "6 см: ${data.soilTemperature6cm?.toInt() ?: "?"}°",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "18 см: ${data.soilTemperature18cm?.toInt() ?: "?"}°",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "54 см: ${data.soilTemperature54cm?.toInt() ?: "?"}°",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Header(WeatherCode.Soil, "Почва: влажность", 1f)
        Text("Почва: влажность (м³/м³)", style = MaterialTheme.typography.bodyMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "0–1 см: ${data.getSoilMoisture0to1cm}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "1–3 см: ${data.getSoilMoisture1to3cm}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "3–9 см: ${data.getSoilMoisture3to9cm}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "9–27 см: ${data.getSoilMoisture9to27cm}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "27–81 см: ${data.getSoilMoisture27to81cm}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text("Прочее", style = MaterialTheme.typography.bodyMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "Обледенение на высоте: ${data.freezingLevelHeight ?: "?"} м",
                style = MaterialTheme.typography.bodySmall
            )
            Text("Потенциальная конвективная энергия: ${data.cape ?: "?"}", style = MaterialTheme.typography.bodySmall)
            Text(
                "Дефицит давления пара: ${data.vapourPressureDeficit ?: "?"} кПа",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Эвапотранспирация: ${data.evapotranspiration ?: "?"} мм",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Эвапотранспирация ФАО: ${data.et0FaoEvapotranspiration ?: "?"} мм",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Потенциал молнии.: ${data.lightningPotential ?: "?"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun Header(
    code: WeatherCode,
    title: String,
    scale: Float
) {
    Row {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                ),
        ) {
            LottieAnimation(
                gif(
                    AnimationType.SIMPLE,
                    code,
                    true
                )
            ) { painter ->
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    painter = painter,
                    contentDescription = ""
                )
            }
        }

        Text(title, style = MaterialTheme.typography.bodyMedium)
    }
}