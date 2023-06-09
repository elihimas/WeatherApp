package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weather.data.model.Forecast

class ForecastWrapper(val forecast: Forecast? = null, status: ResultStatus) : ResultWrapper(status)
