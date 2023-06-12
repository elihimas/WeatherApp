package com.elihimas.weather.citiesrepository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    val name: String
) {
    constructor(name: String) : this(null, name)
}