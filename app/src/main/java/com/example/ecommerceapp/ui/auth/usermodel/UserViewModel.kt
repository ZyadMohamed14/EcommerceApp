package com.example.ecommerceapp.ui.auth.usermodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.reposotiry.auth.firebase.FirebaseAuthRepository
import com.example.ecommerceapp.data.reposotiry.common.AppPreferenceRepository
import com.example.ecommerceapp.data.reposotiry.user.UserFirestoreRepository
import com.example.ecommerceapp.data.reposotiry.user.UserPreferenceRepository
import com.example.ecommerceapp.domain.toUserDetailsModel
import com.example.ecommerceapp.domain.toUserDetailsPreferences
import com.example.ecommerceapp.utils.CrashlyticsUtils
import com.example.ecommerceapp.utils.CrashlyticsUtils.LISTEN_TO_USER_DETAILS
import com.example.ecommerceapp.utils.UserDetailsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.mapLatest as mapLatest

@HiltViewModel
class UserViewModel @Inject constructor(
    private val appPreferencesRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val userFirestoreRepository: UserFirestoreRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val logoutState = MutableSharedFlow<Resource<Unit>>()

    // load user data in state flow inside view model  scope
    val userDetailsState = getUserDetails().stateIn(
        viewModelScope, started = SharingStarted.Eagerly, initialValue = null
    )

    init {
        listenToUserDetails()
    }

    // load user data flow
    // we can use this to get user data in the view in main thread so we do not want to wait the data from state
    // note that this flow block the main thread while you get the data every time you call it
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserDetails() =
        userPreferenceRepository.getUserDetails().mapLatest { it.toUserDetailsModel() }

    private fun listenToUserDetails() = viewModelScope.launch {
        val userId = userPreferenceRepository.getUserId().first()
        if (userId.isEmpty()) return@launch
        userFirestoreRepository.getUserDetails(userId).catch { e ->
            val msg = e.message ?: "Error listening to user details"
            CrashlyticsUtils.sendCustomLogToCrashlytics<UserDetailsException>(
                msg, LISTEN_TO_USER_DETAILS to msg
            )
            if (e is UserDetailsException) logOut()
        }.collectLatest { resource ->
            Log.d(TAG, "listenToUserDetails: ${resource.data}")
            when (resource) {
                is Resource.Success -> {

                    resource.data?.let {
                       // userPreferenceRepository.updateUserDetails(it.toUserDetailsPreferences())
                    }
                }

                else -> {
                    // Do nothing
                    Log.d(TAG, "Error listen to user details: ${resource.exception?.message}")
                }
            }
        }
    }

    suspend fun isUserLoggedIn() = appPreferencesRepository.isLoggedIn()
    suspend fun logOut() = viewModelScope.launch {
        logoutState.emit(Resource.Loading())
        firebaseAuthRepository.logout()
        userPreferenceRepository.clearUserPreferences()
        appPreferencesRepository.saveLoginState(false)
        logoutState.emit(Resource.Success(Unit))
    }

    companion object {
        private const val TAG = "benz"
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: UserViewModel cleared")
    }
}




