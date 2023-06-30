package com.elihimas.weather.data.repository.local

import com.elihimas.weather.data.model.Weather

interface WeatherDatabase {

    suspend fun saveWeather(weather: Weather)
    suspend fun loadWeatherByCity(city: String): Weather?

}
