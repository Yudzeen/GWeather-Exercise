package com.yudzeen.gweatherexercise.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import com.yudzeen.gweatherexercise.repository.auth.InvalidConfirmPasswordException
import com.yudzeen.gweatherexercise.repository.auth.InvalidEmailException
import com.yudzeen.gweatherexercise.repository.auth.InvalidPasswordException
import com.yudzeen.gweatherexercise.ui.auth.register.RegisterViewModel
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
class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel
    @MockK lateinit var authRepository: AuthRepository

    private val standardTestDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(standardTestDispatcher)
        MockKAnnotations.init(this)
        viewModel = RegisterViewModel(authRepository, standardTestDispatcher)
    }

    @After
    fun teardown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `should success if register with valid credentials`() = runTest {
        val email = "email@email.com"
        val password = "Password11!"

        coEvery { authRepository.register(email, password) } returns Unit
        viewModel.register(email, password, password)
        advanceUntilIdle()

        assert(viewModel.registerResult.value is Result.Success)
    }

    @Test
    fun `should fail if register with invalid email`() = runTest {
        val email = "notEmail"
        val password = "Password11!"

        coEvery { authRepository.register(email, password) } returns Unit
        viewModel.register(email, password, password)
        advanceUntilIdle()

        viewModel.registerResult.value.let {
            assert(it is Result.Error && it.exception is InvalidEmailException)
        }
    }

    @Test
    fun `should fail if register with invalid password`() = runTest {
        val email = "email@email.com"
        val password = "weak"

        coEvery { authRepository.register(email, password) } returns Unit
        viewModel.register(email, password, password)
        advanceUntilIdle()

        viewModel.registerResult.value.let {
            assert(it is Result.Error && it.exception is InvalidPasswordException)
        }
    }

    @Test
    fun `should fail if register with invalid confirm password`() = runTest {
        val email = "email@email.com"
        val password = "Password11!"
        val confirmPassword = "notMatching"

        coEvery { authRepository.register(email, password) } returns Unit
        viewModel.register(email, password, confirmPassword)
        advanceUntilIdle()

        viewModel.registerResult.value.let {
            assert(it is Result.Error && it.exception is InvalidConfirmPasswordException)
        }
    }
}