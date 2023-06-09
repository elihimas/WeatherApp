package com.elihimas.weather.citiesrepository

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [City::class], version = 1)
abstract class CitiesDatabase : RoomDatabase() {
    abstract fun citiesDAO(): CitiesDAO
}