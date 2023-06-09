package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteCoordinates(
    @SerializedName("lon")
    val longitude: Float,
    @SerializedName("lat")
    val latitude: Float
)