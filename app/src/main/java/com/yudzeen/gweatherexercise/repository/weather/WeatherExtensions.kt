package com.yudzeen.gweatherexercise.repository.weather

import com.yudzeen.gweatherexercise.data.local.entity.WeatherEntity
import com.yudzeen.gweatherexercise.data.remote.WeatherResponse
import com.yudzeen.gweatherexercise.domain.weather.Weather

fun WeatherResponse.toDomain(timestamp: Long): Weather {
    return Weather(
        city = name,
        country = sys.country,
        temperature = main.temp,
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        condition = weather.firstOrNull()?.main ?: "",
        timestamp = timestamp
    )
}

fun Weather.toEntity(): WeatherEntity {
    return WeatherEntity(
        city = city,
        country = country,
        temperature = temperature,
        sunrise = sunrise,
        sunset = sunset,
        condition = condition,
        timestamp = timestamp
    )
}

fun WeatherEntity.toDomain(): Weather {
    return Weather(
        city = city,
        country = country,
        temperature = temperature,
        sunrise = sunrise,
        sunset = sunset,
        condition = condition,
        timestamp = timestamp
    )
}