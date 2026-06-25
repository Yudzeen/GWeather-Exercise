package com.yudzeen.gweatherexercise.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import com.yudzeen.gweatherexercise.domain.weather.WeatherRepository
import com.yudzeen.gweatherexercise.ui.weather.WeatherViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    @MockK lateinit var authRepository: AuthRepository
    @MockK lateinit var weatherRepository: WeatherRepository

    private val standardTestDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(standardTestDispatcher)
        MockKAnnotations.init(this)
        viewModel = WeatherViewModel(authRepository, weatherRepository, standardTestDispatcher)
    }

    @After
    fun teardown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit success when fetching weather succeeds`() = runTest {
        coEvery { weatherRepository.fetchCurrentWeather(any(), any()) } returns mockk()

        viewModel.fetchCurrentWeather(0.0, 0.0)
        advanceUntilIdle()

        assert(viewModel.fetchCurrentWeatherResult.value is Result.Success)
    }

    @Test
    fun `should emit error when fetching weather fails`() = runTest {
        coEvery { weatherRepository.fetchCurrentWeather(any(), any()) } throws Exception()

        viewModel.fetchCurrentWeather(0.0, 0.0)
        advanceUntilIdle()

        assert(viewModel.fetchCurrentWeatherResult.value is Result.Error)
    }
}