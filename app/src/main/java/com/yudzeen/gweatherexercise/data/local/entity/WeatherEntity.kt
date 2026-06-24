package com.yudzeen.gweatherexercise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val country: String,
    val temperature: Double,
    val sunrise: Long,
    val sunset: Long,
    val condition: String,
    val timestamp: Long
)