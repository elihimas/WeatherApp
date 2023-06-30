package com.elihimas.weather.data.repository

import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.model.ForecastItem
import com.elihimas.weather.data.model.LoadResult
import com.elihimas.weather.data.model.Weather
import com.elihimas.weather.data.repository.local.WeatherDatabase
import com.elihimas.weather.data.repository.remote.WeatherAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.lang.Exception
import java.time.LocalDateTime
import java.time.ZoneOffset

class WeatherRepositoryImpl(
    private val api: WeatherAPI,
    private val dao: WeatherDatabase
) : WeatherRepository {

    override fun loadForecast(city: String): Flow<Forecast> = flow {
        val forecastResponse = api.getForecast(city)

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

    override fun loadWeather(city: String): Flow<LoadResult<Weather>> = flow {

        val lastWeather = dao.loadWeatherByCity(city)

        lastWeather?.let { lastWeather ->
            emit(LoadResult.SuccessResult(lastWeather))
        }

        try {
            val weatherResponse = api.getWeather(city.trim())

            if (weatherResponse.statusCode == 200) {
                val cityName = weatherResponse.cityName
                val temperature = weatherResponse.mainWeatherData.temperature

                val weather = Weather(cityName, temperature)
                emit(LoadResult.SuccessResult(weather))

                dao.saveWeather(weather)
            }
        } catch (e: Exception) {
            if (e is HttpException && e.code() == 404) {
                emit(LoadResult.NotFoundResult)
            } else {
                throw e
            }
        }
    }
}
