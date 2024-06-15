package com.example.ecommerceapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.user.UserDetailsModel
import com.example.ecommerceapp.data.reposotiry.auth.FirebaseAuthRepository
import com.example.ecommerceapp.data.reposotiry.common.AppPreferenceRepository
import com.example.ecommerceapp.data.reposotiry.user.UserPreferenceRepository
import com.example.ecommerceapp.domain.toUserDetailsPreferences
import com.example.ecommerceapp.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appPreferenceRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,

    ) : ViewModel() {
    private val _loginState= MutableSharedFlow<Resource<UserDetailsModel>>()
    val loginState: SharedFlow<Resource<UserDetailsModel>> = _loginState.asSharedFlow()


    val emailState = MutableStateFlow("")
    val passwordState = MutableStateFlow("")
    val isLoginValid: Flow<Boolean> = combine(emailState, passwordState) { email, password ->
        email.isValidEmail() && password.length >= 6
    }


    fun loginWithEmailAndPassword() = viewModelScope.launch(IO) {
        val email = emailState.value
        val password = passwordState.value
        if (isLoginValid.first()) {
           handleLoginFlow { authRepository.loginWithEmailAndPassword(email, password) }
        } else {
            _loginState.emit(Resource.Error(Exception("Invalid email or password")))
        }
    }
    fun loginWithGoogle(idToken: String) {
        handleLoginFlow { authRepository.loginWithGoogle(idToken) }
    }

    fun loginWithFacebook(token: String) {
        handleLoginFlow { authRepository.loginWithFacebook(token) }
    }
    private fun handleLoginFlow(loginFlow: suspend () -> Flow<Resource<UserDetailsModel>>) =
        viewModelScope.launch(IO) {
            loginFlow().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        savePreferenceData(resource.data!!)
                        _loginState.emit(Resource.Success(resource.data))
                    }

                    else -> _loginState.emit(resource)
                }
            }
        }


    private suspend fun savePreferenceData(userDetailsModel: UserDetailsModel) {
        appPreferenceRepository.saveLoginState(true)
        userPreferenceRepository.updateUserDetails(userDetailsModel.toUserDetailsPreferences())
    }

}





