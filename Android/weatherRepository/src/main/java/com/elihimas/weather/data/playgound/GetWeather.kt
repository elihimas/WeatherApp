package com.elihimas.weather.data.playgound

import com.elihimas.weather.data.repository.remote.APIBuilder
import com.elihimas.weather.data.repository.remote.model.GetWeatherResponse
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    println("getWeather example")
    println()

    val weather = getWeather()

    showWeather(weather)
}

suspend fun getWeather(): GetWeatherResponse {
    val city = "Recife"
    val api = APIBuilder.createAPI()

    return api.getWeather(city)
}

fun showWeather(weather: GetWeatherResponse) {
    println("Full weather:")
    println(weather)
}
