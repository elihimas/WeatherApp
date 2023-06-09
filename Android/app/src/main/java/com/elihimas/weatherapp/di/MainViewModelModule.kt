package com.elihimas.weatherapp.di

import android.app.Application
import com.elihimas.weather.citiesrepository.CitiesRepository
import com.elihimas.weather.citiesrepository.CitiesRepositoryImpl
import com.elihimas.weather.data.repository.WeatherRepository
import com.elihimas.weather.data.repository.WeatherRepositoryImpl
import com.elihimas.weather.data.repository.remote.APIBuilder
import com.elihimas.weather.data.repository.remote.WeatherAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MainViewModelModule {

    @Provides
    fun provideWeatherRepository(weatherAPI: WeatherAPI): WeatherRepository {
        return WeatherRepositoryImpl(weatherAPI)
    }

    @Provides
    fun provideWeatherAPI(): WeatherAPI {
        return APIBuilder.createAPI()
    }

    @Provides
    fun providesCitiesRepository(application: Application): CitiesRepository {
        return CitiesRepositoryImpl(application)
    }


}