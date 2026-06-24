package com.yudzeen.gweatherexercise.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Int
) {
    data class Coord(val lon: Double, val lat: Double)

    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

    @JsonClass(generateAdapter = true)
    data class Main(
        val temp: Double,
        @field:Json("feels_like") val feelsLike: Double,
        @field:Json("temp_min") val tempMin: Double,
        @field:Json("temp_max") val tempMax: Double,
        val pressure: Int,
        val humidity: Int,
        @field:Json("sea_level") val seaLevel: Int?,
        @field:Json("grnd_level") val groundLevel: Int?
    )

    data class Wind(
        val speed: Double,
        val deg: Int,
        val gust: Double?
    )

    data class Clouds(val all: Int)

    data class Sys(
        val country: String,
        val sunrise: Long,
        val sunset: Long
    )
}