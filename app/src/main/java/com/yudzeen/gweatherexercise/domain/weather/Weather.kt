package com.yudzeen.gweatherexercise.domain.weather

data class Weather(
    val city: String,
    val country: String,
    val temperature: Double,
    val sunrise: Long,
    val sunset: Long,
    val condition: String,
    val timestamp: Long
)