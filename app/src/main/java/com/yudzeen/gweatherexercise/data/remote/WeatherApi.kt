package com.yudzeen.gweatherexercise.data.remote

import com.yudzeen.gweatherexercise.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = BuildConfig.OPEN_WEATHER_API_KEY
    ): WeatherResponse
}