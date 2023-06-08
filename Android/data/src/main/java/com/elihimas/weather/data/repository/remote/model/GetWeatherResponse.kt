package com.elihimas.weather.data.repository.remote.model

import com.elihimas.weather.data.model.Forecast
import com.google.gson.annotations.SerializedName

data class GetWeatherResponse(
    @SerializedName("cod")
    val statusCode:Int,
    @SerializedName("main")
    val forecastData: RemoteForecastData,
    val weatherList: List<Forecast>
)