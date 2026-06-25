package com.yudzeen.gweatherexercise.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import com.yudzeen.gweatherexercise.repository.auth.UserNotFoundException
import com.yudzeen.gweatherexercise.ui.auth.login.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    @MockK
    lateinit var authRepository: AuthRepository

    private val standardTestDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(standardTestDispatcher)
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(authRepository, standardTestDispatcher)
    }

    @After
    fun teardown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `should success if login with valid credentials`() = runTest {
        coEvery { authRepository.login("email", "password") } returns Unit

        viewModel.login("email", "password")
        advanceUntilIdle()

        assert(viewModel.loginResult.value is Result.Success)
    }

    @Test
    fun `should fail if login with invalid credentials`() = runTest {
        coEvery { authRepository.login("email", "password") } throws UserNotFoundException("User not found.")

        viewModel.login("email", "password")
        advanceUntilIdle()

        assert(viewModel.loginResult.value is Result.Error)
    }


}