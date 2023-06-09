package com.elihimas.weather.citiesrepository

import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    suspend fun allCities(): Flow<List<City>>

    suspend fun insertCity(city: City)
}