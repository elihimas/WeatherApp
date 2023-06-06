package com.elihimas.weather.data.repository

import com.elihimas.weather.data.model.ForecastItem
import com.elihimas.weather.data.model.WeatherData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl : WeatherRepository {

    // TODO: use a weather remote api
    override fun loadData(): Flow<WeatherData> =
        flow {
            delay(2000)
            val result = WeatherData(
                "Recife",
                listOf(
                    ForecastItem(36.0),
                    ForecastItem(37.0),
                    ForecastItem(32.0),
                    ForecastItem(34.0),
                )
            )

            emit(result)
        }
}
