package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteCity(
    val country: String,
    val population: Long,
    @SerializedName("timezone")
    val localSecondsOffset: Int,
    @SerializedName("sunrise")
    val sunriseSeconds: Long,
    @SerializedName("sunset")
    val sunsetSeconds: Long
)
