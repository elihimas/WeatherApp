package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weather.citiesrepository.City

sealed class UiState {
    object Loading : UiState()
    object NoCitiesRegistered : UiState()
    data class Success(val cities: List<City>) : UiState()
    object RetryError : UiState()
    object FinalError : UiState()
}
