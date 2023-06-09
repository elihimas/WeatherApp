package com.elihimas.weatherapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elihimas.weather.citiesrepository.CitiesRepositoryImpl
import com.elihimas.weather.data.repository.WeatherRepositoryImpl
import com.elihimas.weather.data.repository.remote.APIBuilder
import com.elihimas.weatherapp.ui.viewmodels.main.MainViewModel

class ViewModelFactory(application: Application) : ViewModelProvider.Factory {

    private val weatherAPI by lazy { APIBuilder.createAPI() }
    private val citiesRepository by lazy { CitiesRepositoryImpl(application) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(WeatherRepositoryImpl(weatherAPI), citiesRepository) as T
    }
}
