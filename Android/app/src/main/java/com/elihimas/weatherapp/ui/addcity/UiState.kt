package com.elihimas.weatherapp.ui.addcity

import com.elihimas.weather.data.model.Weather

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    class CityFound(val weather: Weather) : UiState()
    object CityNotFound : UiState()
    object CityAdded : UiState()
}
