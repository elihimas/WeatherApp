package com.elihimas.weatherapp.ui.addcity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elihimas.weather.citiesrepository.CitiesRepository
import com.elihimas.weather.citiesrepository.City
import com.elihimas.weather.data.model.LoadResult
import com.elihimas.weather.data.model.Weather
import com.elihimas.weather.data.repository.WeatherRepository
import com.elihimas.weatherapp.util.addRetry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val citiesRepository: CitiesRepository
) :
    ViewModel() {

    val uiState = MutableStateFlow<UiState>(UiState.Idle)

    fun findCity(cityName: String) {
        uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository
                .loadWeather(cityName)
                .addRetry(
                    createFinalError = { LoadResult.NotFoundResult }
                )
                .collect(::handleLoadWeather)
        }
    }

    private fun handleLoadWeather(result: LoadResult<Weather>) {
        when (result) {
            is LoadResult.SuccessResult -> sendFindCitySuccess(result)
            LoadResult.NotFoundResult -> sendCityNotFound()
        }
    }

    private fun sendFindCitySuccess(result: LoadResult.SuccessResult<Weather>) {
        uiState.value = UiState.CityFound(result.resultData)
    }

    private fun sendCityNotFound() {
        uiState.value = UiState.CityNotFound
    }

    fun addCity(cityName: String) {
        uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            citiesRepository.insertCity(City(cityName))
            uiState.value = UiState.CityAdded
        }
    }
}
