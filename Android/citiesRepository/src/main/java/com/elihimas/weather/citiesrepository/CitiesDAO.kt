package com.elihimas.weather.citiesrepository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiesDAO {
    @Query("SELECT * from City")
    fun allCities(): Flow<List<City>>

    @Insert
    fun insertCity(city: City)
}