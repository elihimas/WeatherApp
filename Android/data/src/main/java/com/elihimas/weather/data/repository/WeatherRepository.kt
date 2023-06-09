package com.elihimas.weather.data.repository

import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun loadWeather(): Flow<Weather>
    fun loadForecast(): Flow<Forecast>
}