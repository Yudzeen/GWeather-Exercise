package com.yudzeen.gweatherexercise.repository.weather

import com.yudzeen.gweatherexercise.data.local.dao.WeatherDao
import com.yudzeen.gweatherexercise.data.remote.WeatherApi
import com.yudzeen.gweatherexercise.domain.weather.Weather
import com.yudzeen.gweatherexercise.domain.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
): WeatherRepository {

    override fun getWeatherHistory(): Flow<List<Weather>> {
        return weatherDao.getAllWeather().map { weatherEntities -> weatherEntities.map { it.toDomain() } }
    }

    override suspend fun fetchCurrentWeather(lat: Double, lon: Double) {
        val currentWeather = weatherApi.getCurrentWeather(lat, lon).toDomain(System.currentTimeMillis())
        weatherDao.upsertWeather(currentWeather.toEntity())
    }

}