package com.example.ecommerceapp.data.datasource.repository.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.ecommerceapp.data.datasource.local.DataStoreKeys.IS_USER_LOGGED_IN
import com.example.ecommerceapp.data.datasource.local.DataStoreKeys.USER_ID
import com.example.ecommerceapp.data.datasource.local.appDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(val context: Context) : UserPreferencesRepository {
    override suspend fun isUserLoggedIn(): Flow<Boolean> =
        context.appDataStore.data.map { preferences ->
            // Return the logged-in state, defaulting to false if not found
            preferences[IS_USER_LOGGED_IN] ?: false
        }


    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[IS_USER_LOGGED_IN] = isLoggedIn
        }
    }

    override suspend fun saveUserID(userId: String) {
         context.appDataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }
}