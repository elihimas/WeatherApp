package com.elihimas.weatherapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elihimas.weather.citiesrepository.CitiesRepository
import com.elihimas.weatherapp.ui.viewmodels.main.UiState
import com.elihimas.weatherapp.util.addRetry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository
) : ViewModel() {

    val uiState = MutableStateFlow<UiState>(UiState.Loading)

    init {
        viewModelScope.launch {
            citiesRepository
                .allCities()
                .map { cities ->
                    if (cities.isEmpty()) {
                        UiState.NoCitiesRegistered
                    } else {
                        UiState.Success(cities)
                    }
                }
                .addRetry(
                    createRetryError = { UiState.RetryError },
                    createFinalError = { UiState.FinalError }
                )
                .collect { newState ->
                    uiState.value = newState
                }
        }
    }

    fun onAddClicked() {

    }

}
