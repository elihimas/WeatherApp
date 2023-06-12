package com.elihimas.weatherapp.util

import android.view.View
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun <T> Flow<T>.addRetry(
    createRetryError: (() -> T)? = null,
    createFinalError: () -> T,
) = retryWhen { cause, attempt ->
    val shouldRetry = cause is IOException && attempt < 3

    if (shouldRetry) {
        if (attempt == 0L) {
            delay(200)
        } else {
            delay(1_000 * attempt)
        }
        createRetryError?.let { createRetryError ->
            emit(createRetryError())
        }
    }

    shouldRetry
}
    .catch {
        emit(createFinalError())
    }
