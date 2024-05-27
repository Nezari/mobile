package com.example.weatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = ""

object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    fun create(): WeatherApi{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WeatherApi::class.java)
    }
}
