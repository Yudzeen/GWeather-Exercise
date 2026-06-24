package com.yudzeen.gweatherexercise.repository.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.yudzeen.gweatherexercise.domain.auth.AuthRepository
import com.yudzeen.gweatherexercise.data.local.dao.UserDao
import com.yudzeen.gweatherexercise.data.local.entity.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        return dataStore.data.first()[AUTH_TOKEN] != null
    }

    override suspend fun login(
        email: String,
        password: String
    ) {
        delay(1500L)    // to simulate api call

        val user = userDao.getUser(email, password)

        if (user == null) {
            throw UserNotFoundException("User not found")
        }

        val token = email to password
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token.toString()
        }
    }

    override suspend fun register(
        email: String,
        password: String
    ) {
        delay(1500L)    // to simulate api call

        val user = userDao.getUserByEmail(email)

        if (user != null) {
            throw UserAlreadyExistsException("User already exists.")
        }

        userDao.saveUser(UserEntity(email, password))
    }

    override suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
        }
    }
}