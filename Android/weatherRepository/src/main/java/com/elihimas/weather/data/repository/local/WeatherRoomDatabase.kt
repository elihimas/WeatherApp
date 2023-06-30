package com.elihimas.weather.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elihimas.weather.data.model.Weather

@Database(entities = [Weather::class], version = 1)
abstract class WeatherRoomDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
