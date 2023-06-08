package com.elihimas.weather.data.repository

import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.model.ForecastItem
import com.elihimas.weather.data.repository.remote.WeatherAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(private val api: WeatherAPI) : WeatherRepository {

    override fun loadForecast(): Flow<Forecast> =
        flow {
            val forecastResponse = api.getForecast("Recife")
            val items =
                forecastResponse.forecastList.map { ForecastItem(it.forecastData.temperature) }
            val forecast = Forecast(items)

            emit(forecast)
        }
}
