package com.example.ecommerceapp.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.auth.RegisterRequestModel
import com.example.ecommerceapp.data.model.auth.RegisterResponseModel
import com.example.ecommerceapp.data.model.user.UserDetailsModel
import com.example.ecommerceapp.data.reposotiry.auth.firebase.FirebaseAuthRepository
import com.example.ecommerceapp.data.reposotiry.common.AppPreferenceRepository
import com.example.ecommerceapp.data.reposotiry.user.UserPreferenceRepository
import com.example.ecommerceapp.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appPreferenceRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _registerState = MutableSharedFlow<Resource<UserDetailsModel>>()
    val registerState: SharedFlow<Resource<UserDetailsModel>> = _registerState.asSharedFlow()
    private val _registerStateWithApi = MutableSharedFlow<Resource<RegisterResponseModel>>()
    val registerStateWithApi: SharedFlow<Resource<RegisterResponseModel>> =
        _registerStateWithApi.asSharedFlow()

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
    private val isRegisterIsValid = combine(
        name, email, password, confirmPassword
    ) { name, email, password, confirmPassword ->
        email.isValidEmail() && password.length >= 6 && name.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    }

    fun registerWithEmailAndPassword() = viewModelScope.launch(IO) {
        val name = name.value
        val email = email.value
        val password = password.value
        if (isRegisterIsValid.first()) {
            // handle register flow
            authRepository.registerWithEmailAndPassword(name, email, password).collect {
                _registerState.emit(it)
            }
        } else {
            // emit error
            _registerState.emit(Resource.Error(Exception("Please Check Your Entered Data")))
        }
    }
    fun registerWithEmailAndPasswordWithApi() = viewModelScope.launch(IO) {
        val name = name.value
        val email = email.value
        val password = password.value
        if (isRegisterIsValid.first()) {
            val registerResponseModel = RegisterRequestModel(
                fullName = name,
                email = email,
                password = password
            )
            // handle register flow
            authRepository.registerEmailAndPasswordWithAPI(registerResponseModel).collect {
                _registerStateWithApi.emit(it)

            }
        } else {
            // emit error
            _registerStateWithApi.emit(Resource.Error(Exception("Please Check Your Entered Data")))
        }


    }



    fun signUpWithGoogle(idToken: String) = viewModelScope.launch {
        authRepository.registerWithGoogle(idToken).collect {
            _registerState.emit(it)
        }
    }

    fun registerWithFacebook(token: String) = viewModelScope.launch {
        authRepository.registerWithFacebook(token).collect {
            _registerState.emit(it)
        }
    }
}