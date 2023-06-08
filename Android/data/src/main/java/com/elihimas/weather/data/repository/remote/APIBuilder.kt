package com.elihimas.weather.data.repository.remote

import com.elihimas.weather.data.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object APIBuilder {

    private val timeWithHourGsonBuilder = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm")
        .create()

    private val timeWithDaysGsonBuilder = GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .create()

    fun createAPI(): WeatherAPI = Retrofit.Builder()
        .client(createClient())
        .baseUrl(BuildConfig.baseUrl)
        .addConverterFactory(GsonConverterFactory.create(timeWithHourGsonBuilder))
        .addConverterFactory(GsonConverterFactory.create(timeWithDaysGsonBuilder))
        .build()
        .create()

    private fun createClient(): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl: HttpUrl = original.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("APPID", BuildConfig.apiKey)
                .build()

            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()

            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()


}