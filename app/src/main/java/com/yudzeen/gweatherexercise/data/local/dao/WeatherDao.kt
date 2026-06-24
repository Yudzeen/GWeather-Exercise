package com.yudzeen.gweatherexercise.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.yudzeen.gweatherexercise.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather ORDER BY timestamp DESC")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    @Upsert
    suspend fun upsertWeather(weather: WeatherEntity)
}