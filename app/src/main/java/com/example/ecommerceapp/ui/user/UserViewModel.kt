package com.example.ecommerceapp.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.datasource.repository.user.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    suspend fun isUserLoggedIn() = userPreferencesRepository.isUserLoggedIn()
    fun setIsLoggedIn(userLoginState: Boolean) {
        viewModelScope.launch(IO) {
            userPreferencesRepository.saveLoginState(userLoginState)
        }
    }


}



