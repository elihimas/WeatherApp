package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteWindData(
    val speed: Double,
    @SerializedName("deg")
    val degrees: Int
)
