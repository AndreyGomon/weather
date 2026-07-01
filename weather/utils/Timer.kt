package ru.mascot.features.weather.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class Timer(
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun launch(
        key: String,
        period: Long,
        jitterMinMs: Long = 0L,
        jitterMaxMs: Long = 0L,
        runImmediately: Boolean = false,
        block: suspend () -> Unit
    ): Job = scope.launch(dispatcher) {
        if (runImmediately) runCatching {
            block()
        }

        while (isActive) {
            delay(period)

            if (jitterMaxMs > 0L) {
                val min = jitterMinMs.coerceAtLeast(0L)
                val max = jitterMaxMs.coerceAtLeast(min)
                delay(Random.nextLong(min, max + 1))
            }

            runCatching {
                block()
            }
        }
    }
}