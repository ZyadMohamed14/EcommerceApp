package com.example.ecommerceapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.datasource.repository.user.UserPreferencesRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    fun saveLoginState(isLoggedIn: Boolean) {
        viewModelScope.launch {


        }
    }
}