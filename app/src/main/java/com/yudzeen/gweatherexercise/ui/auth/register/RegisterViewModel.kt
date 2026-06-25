package com.yudzeen.gweatherexercise.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.di.IoDispatcher
import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import com.yudzeen.gweatherexercise.repository.auth.InvalidConfirmPasswordException
import com.yudzeen.gweatherexercise.repository.auth.InvalidEmailException
import com.yudzeen.gweatherexercise.repository.auth.InvalidPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject internal constructor(
    private val authRepository: AuthRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult

    fun register(email: String, password: String, confirmPassword: String) {
        val isValidEmail = validateEmail(email)
        val isValidPassword = validatePassword(password)
        val isValidConfirmPassword = validateConfirmPassword(password, confirmPassword)

        if (!isValidEmail || !isValidPassword || !isValidConfirmPassword) { return }

        viewModelScope.launch(ioDispatcher) {
            _registerResult.postValue(Result.Loading)
            try {
                authRepository.register(email, password)
                _registerResult.postValue(Result.Success(email))
            } catch (e: Exception) {
                _registerResult.postValue(Result.Error(e))
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        val emailRegex = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        )
        if (!emailRegex.matches(email)) {
            _registerResult.value = Result.Error(InvalidEmailException("Invalid email address."))
            return false
        }
        return true
    }

    private fun validatePassword(password: String): Boolean {
        val errors = mutableListOf<String>()

        if (password.length < 8) {
            errors.add("Password must be at least 8 characters long.")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("Password must contain at least one uppercase letter.")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("Password must contain at least one lowercase letter.")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("Password must contain at least one digit.")
        }
        if (!password.any { "!@#\$%^&*()-_=+[{]}|;:',<.>/?".contains(it) }) {
            errors.add("Password must contain at least one special character.")
        }

        if (errors.isNotEmpty()) {
            _registerResult.value = Result.Error(InvalidPasswordException(errors.joinToString("\n")))
            return false
        }

        return true
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            _registerResult.value = (Result.Error(InvalidConfirmPasswordException("Passwords do not match.")))
            return false
        }
        return true
    }

}