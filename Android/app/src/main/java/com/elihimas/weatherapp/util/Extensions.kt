package com.elihimas.weatherapp.util

import android.view.View
import com.elihimas.weather.data.model.WeatherData
import com.elihimas.weatherapp.ui.viewmodels.main.UiState


fun WeatherData.toUiState() = UiState.Success(this)

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}