package com.yudzeen.gweatherexercise.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.di.IoDispatcher
import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import com.yudzeen.gweatherexercise.domain.weather.Weather
import com.yudzeen.gweatherexercise.domain.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject internal constructor(
    private val authRepository: AuthRepository,
    private val weatherRepository: WeatherRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _logoutResult = MutableLiveData<Result<Unit>>()
    val logoutResult: LiveData<Result<Unit>> = _logoutResult

    private val _fetchCurrentWeatherResult = MutableLiveData<Result<Weather>>()
    val fetchCurrentWeatherResult: LiveData<Result<Weather>> = _fetchCurrentWeatherResult

    val weatherHistory = liveData(ioDispatcher) {
        weatherRepository.getWeatherHistory().collect { emit(it) }
    }

    fun logout() {
        viewModelScope.launch(ioDispatcher) {
            _logoutResult.postValue(Result.Loading)
            try {
                authRepository.logout()
                _logoutResult.postValue(Result.Success(Unit))
            } catch (e: Exception) {
                _logoutResult.postValue(Result.Error(e))
            }
        }
    }

    fun fetchCurrentWeather(lat: Double, lon: Double) {
        viewModelScope.launch(ioDispatcher) {
            _fetchCurrentWeatherResult.postValue(Result.Loading)
            try {
                val currentWeather = weatherRepository.fetchCurrentWeather(lat, lon)
                _fetchCurrentWeatherResult.postValue(Result.Success(currentWeather))
            } catch (e: Exception) {
                _fetchCurrentWeatherResult.postValue(Result.Error(e))
            }
        }
    }
}