package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class GetForecastResponse(
    @SerializedName("cod")
    val statusCode: Int,
    @SerializedName("list")
    val forecastList: List<RemoteForecastItem>
)