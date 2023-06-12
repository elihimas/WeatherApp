package com.elihimas.weatherapp.di

import android.app.Application
import com.elihimas.weather.citiesrepository.CitiesRepository
import com.elihimas.weather.citiesrepository.CitiesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun providesCitiesRepository(application: Application): CitiesRepository {
        return CitiesRepositoryImpl(application)
    }

}