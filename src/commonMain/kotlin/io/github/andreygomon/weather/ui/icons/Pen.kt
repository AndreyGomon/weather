package io.github.andreygomon.weather.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WeatherIcons.Pen: ImageVector
    get() {
        if (_Pen != null) {
            return _Pen!!
        }
        _Pen = ImageVector.Builder(
            name = "Pen",
            defaultWidth = 18.dp,
            defaultHeight = 18.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF0B0624)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(10.934f, 1.834f)
                curveTo(12.379f, 0.39f, 14.721f, 0.39f, 16.166f, 1.834f)
                curveTo(17.61f, 3.279f, 17.611f, 5.621f, 16.166f, 7.066f)
                lineTo(9.08f, 14.152f)
                curveTo(8.66f, 14.572f, 8.406f, 14.827f, 8.123f, 15.048f)
                curveTo(7.799f, 15.301f, 7.447f, 15.518f, 7.076f, 15.695f)
                curveTo(6.752f, 15.849f, 6.41f, 15.962f, 5.847f, 16.15f)
                lineTo(2.784f, 17.171f)
                curveTo(2.229f, 17.356f, 1.617f, 17.212f, 1.203f, 16.798f)
                lineTo(1.202f, 16.797f)
                curveTo(0.789f, 16.384f, 0.644f, 15.771f, 0.829f, 15.216f)
                lineTo(1.85f, 12.154f)
                curveTo(2.038f, 11.59f, 2.151f, 11.249f, 2.306f, 10.924f)
                curveTo(2.482f, 10.553f, 2.699f, 10.202f, 2.952f, 9.877f)
                curveTo(3.173f, 9.594f, 3.429f, 9.34f, 3.848f, 8.92f)
                lineTo(10.934f, 1.834f)
                close()
                moveTo(4.909f, 9.981f)
                curveTo(4.464f, 10.426f, 4.286f, 10.606f, 4.135f, 10.799f)
                curveTo(3.949f, 11.038f, 3.789f, 11.297f, 3.659f, 11.57f)
                curveTo(3.553f, 11.792f, 3.472f, 12.032f, 3.273f, 12.628f)
                lineTo(2.599f, 14.65f)
                lineTo(3.35f, 15.401f)
                lineTo(5.372f, 14.727f)
                curveTo(5.969f, 14.528f, 6.209f, 14.447f, 6.431f, 14.341f)
                curveTo(6.703f, 14.211f, 6.963f, 14.052f, 7.201f, 13.866f)
                curveTo(7.395f, 13.715f, 7.574f, 13.536f, 8.019f, 13.091f)
                lineTo(13.493f, 7.617f)
                curveTo(12.93f, 7.384f, 12.261f, 7.001f, 11.63f, 6.371f)
                curveTo(10.998f, 5.739f, 10.616f, 5.07f, 10.383f, 4.506f)
                lineTo(4.909f, 9.981f)
                close()
                moveTo(15.105f, 2.895f)
                curveTo(14.246f, 2.036f, 12.854f, 2.036f, 11.995f, 2.895f)
                lineTo(11.572f, 3.317f)
                curveTo(11.597f, 3.427f, 11.632f, 3.563f, 11.684f, 3.715f)
                curveTo(11.84f, 4.164f, 12.135f, 4.754f, 12.69f, 5.31f)
                curveTo(13.246f, 5.866f, 13.837f, 6.16f, 14.285f, 6.316f)
                curveTo(14.437f, 6.369f, 14.572f, 6.402f, 14.682f, 6.427f)
                lineTo(15.105f, 6.005f)
                curveTo(15.964f, 5.147f, 15.964f, 3.754f, 15.105f, 2.895f)
                close()
            }
        }.build()

        return _Pen!!
    }

@Suppress("ObjectPropertyName")
private var _Pen: ImageVector? = null
