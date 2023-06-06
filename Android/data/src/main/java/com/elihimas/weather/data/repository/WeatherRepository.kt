package com.elihimas.weather.data.repository

import com.elihimas.weather.data.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun loadData(): Flow<WeatherData>
}