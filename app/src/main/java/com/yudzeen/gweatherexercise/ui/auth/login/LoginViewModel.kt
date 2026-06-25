package com.yudzeen.gweatherexercise.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.di.IoDispatcher
import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject internal constructor(
    private val authRepository: AuthRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult

    private val _activeSessionResult = MutableLiveData<Result<Unit>>()
    val activeSessionResult: LiveData<Result<Unit>> = _activeSessionResult

    fun checkIfHasActiveSession() {
        viewModelScope.launch(ioDispatcher) {
            val isLoggedIn = authRepository.isLoggedIn()
            if (isLoggedIn) {
                _activeSessionResult.postValue(Result.Success(Unit))
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(ioDispatcher) {
            _loginResult.postValue(Result.Loading)
            try {
                authRepository.login(email, password)
                _loginResult.postValue(Result.Success(Unit))
            } catch (e: Exception) {
                _loginResult.postValue(Result.Error(e))
            }
        }
    }
}