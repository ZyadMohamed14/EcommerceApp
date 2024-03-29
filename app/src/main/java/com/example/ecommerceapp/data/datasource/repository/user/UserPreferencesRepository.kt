package com.example.ecommerceapp.data.datasource.repository.user

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun isUserLoggedIn(): Flow<Boolean>
    suspend fun saveLoginState(isLoggedIn: Boolean)
    suspend fun saveUserID(userId: String)
}