package io.github.andreygomon.weather.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WeatherIcons.InfoDanger: ImageVector
    get() {
        if (_InfoDanger != null) {
            return _InfoDanger!!
        }
        _InfoDanger = ImageVector.Builder(
            name = "InfoDanger",
            defaultWidth = 18.dp,
            defaultHeight = 18.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            path(fill = SolidColor(Color(0xFF0B0624))) {
                moveTo(9f, 11.25f)
                curveTo(9.414f, 11.25f, 9.75f, 11.586f, 9.75f, 12f)
                curveTo(9.75f, 12.414f, 9.414f, 12.75f, 9f, 12.75f)
                curveTo(8.586f, 12.75f, 8.25f, 12.414f, 8.25f, 12f)
                curveTo(8.25f, 11.586f, 8.586f, 11.25f, 9f, 11.25f)
                close()
            }
            path(fill = SolidColor(Color(0xFF0B0624))) {
                moveTo(9f, 4.5f)
                curveTo(9.414f, 4.5f, 9.75f, 4.836f, 9.75f, 5.25f)
                verticalLineTo(9.75f)
                curveTo(9.75f, 10.164f, 9.414f, 10.5f, 9f, 10.5f)
                curveTo(8.586f, 10.5f, 8.25f, 10.164f, 8.25f, 9.75f)
                verticalLineTo(5.25f)
                curveTo(8.25f, 4.836f, 8.586f, 4.5f, 9f, 4.5f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF0B0624)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(11.586f, 1.25f)
                curveTo(12.05f, 1.25f, 12.495f, 1.435f, 12.823f, 1.763f)
                lineTo(16.237f, 5.177f)
                curveTo(16.566f, 5.505f, 16.75f, 5.95f, 16.75f, 6.414f)
                verticalLineTo(11.586f)
                curveTo(16.75f, 12.05f, 16.566f, 12.495f, 16.237f, 12.823f)
                lineTo(12.823f, 16.237f)
                curveTo(12.495f, 16.566f, 12.05f, 16.75f, 11.586f, 16.75f)
                horizontalLineTo(6.414f)
                curveTo(5.95f, 16.75f, 5.505f, 16.566f, 5.177f, 16.237f)
                lineTo(1.763f, 12.823f)
                curveTo(1.435f, 12.495f, 1.25f, 12.05f, 1.25f, 11.586f)
                verticalLineTo(6.414f)
                curveTo(1.25f, 5.95f, 1.435f, 5.505f, 1.763f, 5.177f)
                lineTo(5.177f, 1.763f)
                curveTo(5.505f, 1.435f, 5.95f, 1.25f, 6.414f, 1.25f)
                horizontalLineTo(11.586f)
                close()
                moveTo(6.414f, 2.75f)
                curveTo(6.348f, 2.75f, 6.284f, 2.776f, 6.237f, 2.823f)
                lineTo(2.823f, 6.237f)
                curveTo(2.776f, 6.284f, 2.75f, 6.348f, 2.75f, 6.414f)
                verticalLineTo(11.586f)
                curveTo(2.75f, 11.652f, 2.776f, 11.716f, 2.823f, 11.763f)
                lineTo(6.237f, 15.177f)
                curveTo(6.284f, 15.224f, 6.348f, 15.25f, 6.414f, 15.25f)
                horizontalLineTo(11.586f)
                curveTo(11.652f, 15.25f, 11.716f, 15.224f, 11.763f, 15.177f)
                lineTo(15.177f, 11.763f)
                curveTo(15.224f, 11.716f, 15.25f, 11.652f, 15.25f, 11.586f)
                verticalLineTo(6.414f)
                curveTo(15.25f, 6.348f, 15.224f, 6.284f, 15.177f, 6.237f)
                lineTo(11.763f, 2.823f)
                curveTo(11.716f, 2.776f, 11.652f, 2.75f, 11.586f, 2.75f)
                horizontalLineTo(6.414f)
                close()
            }
        }.build()

        return _InfoDanger!!
    }

@Suppress("ObjectPropertyName")
private var _InfoDanger: ImageVector? = null
