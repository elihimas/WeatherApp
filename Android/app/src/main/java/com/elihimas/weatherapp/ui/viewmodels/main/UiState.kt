package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weather.data.model.Forecast

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: Forecast) : UiState()
    object RetryError : UiState()
    object FinalError : UiState()
}
