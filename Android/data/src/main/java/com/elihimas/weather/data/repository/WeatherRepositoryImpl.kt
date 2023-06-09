package com.elihimas.weather.data.repository

import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.model.ForecastItem
import com.elihimas.weather.data.model.Weather
import com.elihimas.weather.data.repository.remote.WeatherAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.ZoneOffset

class WeatherRepositoryImpl(private val api: WeatherAPI) : WeatherRepository {

    override fun loadForecast(): Flow<Forecast> =
        flow {
            val forecastResponse = api.getForecast("Recife")

            val offset = forecastResponse.city.localSecondsOffset
            val items = forecastResponse.forecastList

                .map { remoteForecastItem ->

                    val date = LocalDateTime.ofEpochSecond(
                        remoteForecastItem.dateTime, 0,
                        ZoneOffset.ofTotalSeconds(offset)
                    )
                    ForecastItem(
                        remoteForecastItem.forecastData.temperature,
                        date
                    )
                }
            val forecast = Forecast(items)

            emit(forecast)
        }

    override fun loadWeather(): Flow<Weather> =
        flow {
            val weatherResponse = api.getWeather("Recife")
            val city = weatherResponse.cityName
            val temperature = weatherResponse.mainWeatherData.temperature

            emit(Weather(city, temperature))
        }
}
