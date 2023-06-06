package com.elihimas.weatherapp.ui.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elihimas.weather.data.model.WeatherData
import com.elihimas.weather.data.repository.WeatherRepository
import com.elihimas.weatherapp.util.toUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(weatherRepository: WeatherRepository) : ViewModel() {

    val uiState =
        weatherRepository
            .loadData()
            .map(WeatherData::toUiState)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                UiState.Loading
            )
}
