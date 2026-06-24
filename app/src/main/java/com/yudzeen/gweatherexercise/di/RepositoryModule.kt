package com.yudzeen.gweatherexercise.di

import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import com.yudzeen.gweatherexercise.domain.weather.WeatherRepository
import com.yudzeen.gweatherexercise.repository.auth.AuthRepositoryImpl
import com.yudzeen.gweatherexercise.repository.weather.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    fun provideWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

}