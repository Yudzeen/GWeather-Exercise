package com.yudzeen.gweatherexercise.domain.weather

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherHistory(): Flow<List<Weather>>
    suspend fun fetchCurrentWeather(lat: Double, lon: Double): Weather
}