package com.elihimas.weather.data.repository

import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.model.LoadResult
import com.elihimas.weather.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun loadWeather(city: String): Flow<LoadResult<Weather>>
    fun loadForecast(city: String): Flow<Forecast>
}