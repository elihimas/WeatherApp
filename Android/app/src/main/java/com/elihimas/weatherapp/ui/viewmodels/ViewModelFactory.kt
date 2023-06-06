package com.elihimas.weatherapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elihimas.weather.data.repository.WeatherRepositoryImpl
import com.elihimas.weatherapp.ui.viewmodels.main.MainViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(WeatherRepositoryImpl()) as T
    }
}
