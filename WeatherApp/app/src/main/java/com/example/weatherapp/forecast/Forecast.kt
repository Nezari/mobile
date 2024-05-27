package com.example.weatherapp.forecast

import com.example.weatherapp.db.Weather

data class Forecast(
    val dt: Int,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double
)

data class ForecastResponse(val list: List<Forecast>)
