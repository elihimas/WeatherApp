package com.elihimas.weather.citiesrepository

import android.app.Application
import androidx.room.Room


class CitiesRepositoryImpl(application: Application) : CitiesRepository {

    private val db =
        Room.databaseBuilder(application, CitiesDatabase::class.java, "cities-database")
            .build()

    override suspend fun allCities() =
        db.citiesDAO().allCities()

    override suspend fun insertCity(city: City) = db.citiesDAO().insertCity(city)

}