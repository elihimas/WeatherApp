package com.elihimas.weather.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elihimas.weather.data.model.Weather

@Dao
interface WeatherDao {

    @Insert
    suspend fun insertWeather(weather: Weather)

    @Query("SELECT * FROM Weather WHERE city = :city")
    suspend fun findWeatherByCityName(city: String): Weather?
}
