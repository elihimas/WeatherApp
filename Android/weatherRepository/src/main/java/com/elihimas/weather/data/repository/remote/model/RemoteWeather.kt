package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteWeather(
    val id: Int,
    @SerializedName("main")
    val mainWeather: String,
    val description: String,
    val icon: String
)