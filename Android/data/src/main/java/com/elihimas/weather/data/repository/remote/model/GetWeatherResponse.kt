package com.elihimas.weather.data.repository.remote.model

import com.google.gson.annotations.SerializedName

data class GetWeatherResponse(
    @SerializedName("cod")
    val statusCode: Int,
    @SerializedName("main")
    val mainWeatherData: RemoteMainWeatherData,
    @SerializedName("name")
    val cityName:String,
    @SerializedName("dt")
    val dateTime: Long,
    @SerializedName("timezone")
    val localSecondsOffset: Int,
    @SerializedName("coord")
    val coordinates: RemoteCoordinates,
    val weather: List<RemoteWeather>,
    val visibility: Int,
    @SerializedName("wind")
    val windData: RemoteWindData,
    val sys:RemoteSysData
)