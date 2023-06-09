package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteMainWeatherData(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("feels_like")
    val feelsLikeTemperature: Double,
    @SerializedName("temp_min")
    val minTemperature: Double,
    @SerializedName("temp_max")
    val maxTemperature: Double,
    val pressure: Int,
    val humidity: Int
)