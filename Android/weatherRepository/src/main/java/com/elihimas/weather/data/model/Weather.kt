package com.elihimas.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey
    val id: Int?,
    val city: String,
    val temperature: Double
) {
    constructor(city: String, temperature: Double) : this(null, city, temperature)
}
