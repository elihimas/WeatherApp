package com.elihimas.weather.data.playgound

import com.elihimas.weather.data.repository.remote.APIBuilder
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    println("API test")

    val api = APIBuilder.createAPI()
    val city = "Recife"

    val weather = api.getWeather(city)
    val forecast = api.getForecast(city)

    println(weather)
    println(forecast)
}