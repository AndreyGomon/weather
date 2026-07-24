package io.github.andreygomon.weather.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.github.andreygomon.weather.resources.Res

@Suppress("ComposableNaming")
@Composable
fun LottieAnimation(
    path: String,
    content: @Composable (Painter) -> Unit
) {
    val json by produceState(initialValue = "", path) {
        value = withContext(Dispatchers.IO) {
            Res.readBytes(path).decodeToString()
        }
    }

    if (json.isEmpty()) return

    key(path, json) {
        val spec = remember(json) { LottieCompositionSpec.JsonString(json) }
        val composition = rememberLottieComposition { spec }


        val painter = rememberLottiePainter(
            composition = composition.value,
            iterations = Compottie.IterateForever
        )

        content(painter)
    }
}