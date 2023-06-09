package com.elihimas.weather.citiesrepository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @PrimaryKey
    val id: Long,
    val name: String
)