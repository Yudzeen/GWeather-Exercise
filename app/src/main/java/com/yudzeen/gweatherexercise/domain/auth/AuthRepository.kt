package com.yudzeen.gweatherexercise.domain.auth

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun logout()
}