package com.elihimas.weatherapp.ui.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elihimas.weather.citiesrepository.CitiesRepository
import com.elihimas.weather.citiesrepository.City
import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.repository.WeatherRepository
import com.elihimas.weatherapp.models.MainData
import com.elihimas.weatherapp.util.addRetry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val citiesRepository: CitiesRepository
) : ViewModel() {

    val uiState = MutableStateFlow<UiState>(UiState.Loading)

    init {
        viewModelScope.launch {
            citiesRepository.allCities().collect { cities ->
                if (cities.isEmpty()) {
                    sendAddCity()
                } else {
                    loadCityWeatherAndForecast(cities.first())
                }
            }
        }
    }

    private fun sendAddCity() {
        uiState.value = UiState.NoCitiesRegistered
    }

    private fun loadCityWeatherAndForecast(city: City) {
        uiState.value = UiState.Loading
        // TODO: load city data
    }

    private fun createForecastFlow() = weatherRepository
        .loadForecast()
        .map(::trimEntries)
        .map { forecast ->
            ForecastWrapper(forecast, ResultStatus.Success)
        }
        .addRetry(
            createRetryError = { ForecastWrapper(status = ResultStatus.RetryError) },
            createFinalError = { ForecastWrapper(status = ResultStatus.FinalError) })
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            ForecastWrapper(status = ResultStatus.Loading)
        )


    private fun createWeatherFlow() = weatherRepository
        .loadWeather()
        .map { weather ->
            WeatherWrapper(weather, ResultStatus.Success)
        }
        .addRetry(
            createRetryError = { WeatherWrapper(status = ResultStatus.RetryError) },
            createFinalError = { WeatherWrapper(status = ResultStatus.FinalError) })
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            WeatherWrapper(status = ResultStatus.Loading)
        )

    // TODO: implement a more insistent retry approach (if some of the network calls fail, retry)
    // TODO: use the forecast and weather flows to feed the ui state that is currently being useg
    private fun createUiState(): Flow<UiState> =
        combine(
            createForecastFlow(),
            createWeatherFlow()
        ) { forecastWrapper: ForecastWrapper, weatherWrapper: WeatherWrapper ->
            val forecast = forecastWrapper.forecast
            val weather = weatherWrapper.weather

            return@combine if (forecast != null && weather != null) {
                UiState.Success(MainData(weather, forecast))
            } else if (forecastWrapper.status == ResultStatus.RetryError || weatherWrapper.status == ResultStatus.RetryError) {
                UiState.RetryError
            } else if (forecastWrapper.status == ResultStatus.FinalError || weatherWrapper.status == ResultStatus.FinalError) {
                UiState.FinalError
            } else {
                UiState.Loading
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            UiState.Loading
        )

    private fun trimEntries(forecast: Forecast): Forecast {
        val trimmedItems = forecast.forecastItems.distinctBy { forecastItem ->
            forecastItem.date.dayOfMonth
        }
        return forecast.copy(forecastItems = trimmedItems)
    }

}
