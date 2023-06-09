package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteForecastItem(
    @SerializedName("dt")
    val dateTime: Long,
    @SerializedName("main")
    val forecastData: RemoteForecastData
)