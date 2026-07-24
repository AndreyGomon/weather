package io.github.andreygomon.weather.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WeatherIcons.Weather: ImageVector
    get() {
        if (_Weather != null) {
            return _Weather!!
        }
        _Weather = ImageVector.Builder(
            name = "Weather",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.334f
            ) {
                moveToRelative(10.095f, 14.416f)
                curveToRelative(2.432f, 0f, 4.404f, -1.972f, 4.404f, -4.404f)
                curveToRelative(0f, -2.432f, -1.972f, -4.404f, -4.404f, -4.404f)
                curveToRelative(-2.432f, 0f, -4.404f, 1.972f, -4.404f, 4.404f)
                curveToRelative(0f, 2.432f, 1.972f, 4.404f, 4.404f, 4.404f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.266f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveToRelative(3.27f, 3.242f)
                lineToRelative(2.074f, 2.074f)
            }
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.266f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveToRelative(16.769f, 3.279f)
                lineToRelative(-2.074f, 2.074f)
            }
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.266f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(19.397f, 10.095f)
                horizontalLineTo(16.464f)
            }
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.266f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(3.524f, 10.095f)
                horizontalLineTo(0.591f)
            }
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.266f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(16.749f, 16.559f)
                lineTo(14.676f, 14.485f)
            }
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.266f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveToRelative(3.319f, 16.598f)
                lineToRelative(2.074f, -2.074f)
            }
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.266f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(10.095f, 3.521f)
                verticalLineTo(0.588f)
            }
            path(
                stroke = SolidColor(Color(0xFF210624)),
                strokeLineWidth = 1.266f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(10.095f, 19.4f)
                verticalLineTo(16.468f)
            }
        }.build()

        return _Weather!!
    }

@Suppress("ObjectPropertyName")
private var _Weather: ImageVector? = null
