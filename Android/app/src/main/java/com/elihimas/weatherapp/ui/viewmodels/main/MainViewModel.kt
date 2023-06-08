package com.elihimas.weatherapp.ui.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.repository.WeatherRepository
import com.elihimas.weatherapp.util.toUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.io.IOException

class MainViewModel(
    weatherRepository: WeatherRepository
) : ViewModel() {
    val uiState =
        weatherRepository
            .loadForecast()
            .map(Forecast::toUiState)
            .retryWhen { cause, attempt ->
                Timber.e(cause)

                val shouldRetry = cause is IOException && attempt < 3

                if (shouldRetry) {
                    if (attempt == 0L) {
                        delay(200)
                    } else {
                        delay(1_000 * attempt)
                    }
                    emit(UiState.RetryError)
                }

                return@retryWhen shouldRetry
            }
            .catch { cause ->
                Timber.e(cause)
                emit(UiState.FinalError)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                UiState.Loading
            )

}
