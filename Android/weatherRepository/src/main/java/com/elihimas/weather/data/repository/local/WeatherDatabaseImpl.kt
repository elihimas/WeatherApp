package com.elihimas.weather.data.repository.local

import android.content.Context
import androidx.room.Room
import com.elihimas.weather.data.model.Weather

class WeatherDatabaseImpl(context: Context) : WeatherDatabase {

    private val db =
        Room.databaseBuilder(context, WeatherRoomDatabase::class.java, "weatherDatabase").build()

    override suspend fun saveWeather(weather: Weather) {
        db.weatherDao().insertWeather(weather)
    }

    override suspend fun loadWeatherByCity(city: String): Weather? =
        db.weatherDao().findWeatherByCityName(city)

}
