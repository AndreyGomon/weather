package ru.mascot.features.weather.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color

@Composable
fun XDebug(
    content: @Composable (Modifier) -> Unit
) {
    var trigger by remember { mutableIntStateOf(0) }

    SideEffect { trigger++ }

    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        alphaAnim.snapTo(0.35f)
        withFrameNanos { }
        alphaAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 450, easing = LinearEasing)
        )
    }

    val alpha = alphaAnim.value

    val debugModifier = Modifier.drawBehind {
        if (alpha > 0f) {
            drawRect(Color.Magenta.copy(alpha = alpha))
        }
    }

    content(debugModifier)
}
