package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteForecastData(
    @SerializedName("temp")
    val temperature: Double
)