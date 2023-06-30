package com.elihimas.weatherapp.di

import android.content.Context
import com.elihimas.weather.data.repository.WeatherRepository
import com.elihimas.weather.data.repository.WeatherRepositoryImpl
import com.elihimas.weather.data.repository.local.WeatherDatabase
import com.elihimas.weather.data.repository.local.WeatherDatabaseImpl
import com.elihimas.weather.data.repository.remote.APIBuilder
import com.elihimas.weather.data.repository.remote.WeatherAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object MainViewModelModule {

    @Provides
    fun provideWeatherRepository(
        weatherAPI: WeatherAPI,
        weatherDAO: WeatherDatabase
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherAPI, weatherDAO)
    }

    @Provides
    fun provideWeatherAPI(): WeatherAPI {
        return APIBuilder.createAPI()
    }

    @Provides
    fun provideWeatherDAO(@ApplicationContext appContext: Context): WeatherDatabase {
        return WeatherDatabaseImpl(appContext)
    }

}