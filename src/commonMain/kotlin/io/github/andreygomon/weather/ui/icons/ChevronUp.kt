package io.github.andreygomon.weather.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WeatherIcons.ChevronUp: ImageVector
    get() {
        if (_ChevronUp != null) {
            return _ChevronUp!!
        }
        _ChevronUp = ImageVector.Builder(
            name = "ChevronUp",
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
                moveTo(14.25f, 11.25f)
                lineTo(9f, 6.75f)
                lineTo(3.75f, 11.25f)
            }
        }.build()

        return _ChevronUp!!
    }

@Suppress("ObjectPropertyName")
private var _ChevronUp: ImageVector? = null
