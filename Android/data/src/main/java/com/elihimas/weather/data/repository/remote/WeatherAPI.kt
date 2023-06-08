package com.elihimas.weather.data.repository.remote

import com.elihimas.weather.data.repository.remote.model.GetForecastResponse
import com.elihimas.weather.data.repository.remote.model.GetWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather?units=metric&cnt=7")
    suspend fun getWeather(@Query("q") query: String): GetWeatherResponse

    @GET("forecast?units=metric&cnt=7")
    suspend fun getForecast(@Query("q") query: String): GetForecastResponse

}