package com.elihimas.weatherapp.ui.viewmodels.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elihimas.weather.citiesrepository.City
import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.model.LoadResult
import com.elihimas.weather.data.repository.WeatherRepository
import com.elihimas.weatherapp.ui.viewmodels.main.ForecastWrapper
import com.elihimas.weatherapp.ui.viewmodels.main.ResultStatus
import com.elihimas.weatherapp.ui.viewmodels.main.WeatherWrapper
import com.elihimas.weatherapp.util.addRetry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityWeatherViewModel  @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    val uiState = MutableStateFlow<UiState>(UiState.Loading)

    private fun loadCityWeatherAndForecast(city: City) {
        uiState.value = UiState.Loading

        // TODO: implement a more insistent redflows to feed the ui state that is currently being useg
        viewModelScope.launch {
            combine(
                createForecastFlow(city),
                createWeatherFlow(city)
            )
            { forecastWrapper: ForecastWrapper, weatherWrapper: WeatherWrapper ->
                val forecast = forecastWrapper.forecast
                val weather = weatherWrapper.weather

                if (forecast != null && weather != null) {
                    UiState.Success(CityData(weather, forecast))
                } else if (forecastWrapper.status == ResultStatus.RetryError || weatherWrapper.status == ResultStatus.RetryError) {
                    UiState.RetryError
                } else if (forecastWrapper.status == ResultStatus.FinalError || weatherWrapper.status == ResultStatus.FinalError) {
                    UiState.FinalError
                } else {
                    UiState.Loading
                }
            }.collect { newState ->
                uiState.value = newState
            }
        }
    }

    private fun createForecastFlow(city: City) = weatherRepository
        .loadForecast(city.name)
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


    private fun createWeatherFlow(city: City) = weatherRepository
        .loadWeather(city.name)
        .map {
            when (it) {
                LoadResult.NotFoundResult -> null
                is LoadResult.SuccessResult -> it.resultData
            }
        }
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

    private fun trimEntries(forecast: Forecast): Forecast {
        val trimmedItems = forecast.forecastItems.distinctBy { forecastItem ->
            forecastItem.date.dayOfMonth
        }
        return forecast.copy(forecastItems = trimmedItems)
    }

    fun loadCity(city: String) {
        loadCityWeatherAndForecast(City(city))
    }
}
