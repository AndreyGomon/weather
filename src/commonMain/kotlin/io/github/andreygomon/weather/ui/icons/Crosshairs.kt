package io.github.andreygomon.weather.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WeatherIcons.Crosshairs: ImageVector
    get() {
        if (_Crosshairs != null) {
            return _Crosshairs!!
        }
        _Crosshairs = ImageVector.Builder(
            name = "Crosshairs",
            defaultWidth = 18.dp,
            defaultHeight = 18.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(18f)
                    verticalLineToRelative(18f)
                    horizontalLineToRelative(-18f)
                    close()
                }
            ) {
                path(
                    fill = SolidColor(Color(0xFF0B0624)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(9f, 6f)
                    curveTo(10.657f, 6f, 12f, 7.343f, 12f, 9f)
                    curveTo(12f, 10.657f, 10.657f, 12f, 9f, 12f)
                    curveTo(7.343f, 12f, 6f, 10.657f, 6f, 9f)
                    curveTo(6f, 7.343f, 7.343f, 6f, 9f, 6f)
                    close()
                    moveTo(9f, 7.5f)
                    curveTo(8.172f, 7.5f, 7.5f, 8.172f, 7.5f, 9f)
                    curveTo(7.5f, 9.828f, 8.172f, 10.5f, 9f, 10.5f)
                    curveTo(9.828f, 10.5f, 10.5f, 9.828f, 10.5f, 9f)
                    curveTo(10.5f, 8.172f, 9.828f, 7.5f, 9f, 7.5f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF0B0624)),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(9f, 0.25f)
                    curveTo(9.414f, 0.25f, 9.75f, 0.586f, 9.75f, 1f)
                    verticalLineTo(1.788f)
                    curveTo(13.155f, 2.138f, 15.862f, 4.845f, 16.212f, 8.25f)
                    horizontalLineTo(17f)
                    curveTo(17.414f, 8.25f, 17.75f, 8.586f, 17.75f, 9f)
                    curveTo(17.75f, 9.414f, 17.414f, 9.75f, 17f, 9.75f)
                    horizontalLineTo(16.212f)
                    curveTo(15.862f, 13.155f, 13.155f, 15.861f, 9.75f, 16.211f)
                    verticalLineTo(17f)
                    curveTo(9.75f, 17.414f, 9.414f, 17.75f, 9f, 17.75f)
                    curveTo(8.586f, 17.75f, 8.25f, 17.414f, 8.25f, 17f)
                    verticalLineTo(16.211f)
                    curveTo(4.845f, 15.861f, 2.138f, 13.155f, 1.788f, 9.75f)
                    horizontalLineTo(1f)
                    curveTo(0.586f, 9.75f, 0.25f, 9.414f, 0.25f, 9f)
                    curveTo(0.25f, 8.586f, 0.586f, 8.25f, 1f, 8.25f)
                    horizontalLineTo(1.788f)
                    curveTo(2.138f, 4.845f, 4.845f, 2.138f, 8.25f, 1.788f)
                    verticalLineTo(1f)
                    curveTo(8.25f, 0.586f, 8.586f, 0.25f, 9f, 0.25f)
                    close()
                    moveTo(9f, 3.25f)
                    curveTo(5.824f, 3.25f, 3.25f, 5.824f, 3.25f, 9f)
                    curveTo(3.25f, 12.176f, 5.824f, 14.75f, 9f, 14.75f)
                    curveTo(12.176f, 14.75f, 14.75f, 12.176f, 14.75f, 9f)
                    curveTo(14.75f, 5.824f, 12.176f, 3.25f, 9f, 3.25f)
                    close()
                }
            }
        }.build()

        return _Crosshairs!!
    }

@Suppress("ObjectPropertyName")
private var _Crosshairs: ImageVector? = null
