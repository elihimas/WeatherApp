package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weatherapp.models.MainData

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: MainData) : UiState()
    object RetryError : UiState()
    object FinalError : UiState()
}
