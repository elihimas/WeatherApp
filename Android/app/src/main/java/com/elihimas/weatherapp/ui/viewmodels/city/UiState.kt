package com.elihimas.weatherapp.ui.viewmodels.city


sealed class UiState {
    object Loading : UiState()
    data class Success(val data: CityData) : UiState()
    object RetryError : UiState()
    object FinalError : UiState()
}
