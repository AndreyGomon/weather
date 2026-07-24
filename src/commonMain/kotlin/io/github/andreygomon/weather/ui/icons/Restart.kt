package io.github.andreygomon.weather.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WeatherIcons.Restart: ImageVector
    get() {
        if (_Restart != null) {
            return _Restart!!
        }
        _Restart = ImageVector.Builder(
            name = "Restart",
            defaultWidth = 18.dp,
            defaultHeight = 18.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0B0624)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13.773f, 6.038f)
                lineTo(13.243f, 5.507f)
                curveTo(10.899f, 3.164f, 7.101f, 3.164f, 4.757f, 5.507f)
                curveTo(2.414f, 7.85f, 2.414f, 11.649f, 4.757f, 13.993f)
                curveTo(7.101f, 16.336f, 10.899f, 16.336f, 13.243f, 13.993f)
                curveTo(14.605f, 12.63f, 15.175f, 10.775f, 14.953f, 9f)
                moveTo(13.773f, 6.038f)
                horizontalLineTo(10.591f)
                moveTo(13.773f, 6.038f)
                verticalLineTo(2.856f)
            }
        }.build()

        return _Restart!!
    }

@Suppress("ObjectPropertyName")
private var _Restart: ImageVector? = null
