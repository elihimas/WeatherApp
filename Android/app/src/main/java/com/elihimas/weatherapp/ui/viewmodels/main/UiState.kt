package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weather.data.model.WeatherData

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: WeatherData) : UiState()
}
