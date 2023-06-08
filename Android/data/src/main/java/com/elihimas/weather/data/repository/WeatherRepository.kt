package com.elihimas.weather.data.repository

import com.elihimas.weather.data.model.Forecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun loadForecast(): Flow<Forecast>
}