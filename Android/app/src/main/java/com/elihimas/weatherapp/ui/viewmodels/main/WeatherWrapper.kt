package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weather.data.model.Weather

class WeatherWrapper(val weather: Weather? = null, status: ResultStatus) : ResultWrapper(status)
