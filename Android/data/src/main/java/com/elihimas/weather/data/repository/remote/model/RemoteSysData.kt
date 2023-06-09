package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteSysData(
    val country: String,
    @SerializedName("sunrise")
    val sunriseDateTime: Long,
    @SerializedName("sunset")
    val sunsetDateTime: Long
)