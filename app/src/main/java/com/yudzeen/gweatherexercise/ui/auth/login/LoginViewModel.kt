package com.yudzeen.gweatherexercise.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject internal constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult

    private val _activeSessionResult = MutableLiveData<Result<Unit>>()
    val activeSessionResult: LiveData<Result<Unit>> = _activeSessionResult

    fun checkIfHasActiveSession() {
        viewModelScope.launch {
            val isLoggedIn = authRepository.isLoggedIn()
            if (isLoggedIn) {
                _activeSessionResult.value = Result.Success(Unit)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            try {
                authRepository.login(email, password)
                _loginResult.value = Result.Success(Unit)
            } catch (e: Exception) {
                _loginResult.value = Result.Error(e)
            }
        }
    }
}