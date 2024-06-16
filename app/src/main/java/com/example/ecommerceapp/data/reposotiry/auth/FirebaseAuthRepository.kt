package com.example.ecommerceapp.data.reposotiry.auth

import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.auth.RegisterRequestModel
import com.example.ecommerceapp.data.model.auth.RegisterResponseModel
import com.example.ecommerceapp.data.model.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<UserDetailsModel>>

    suspend fun loginWithGoogle(
        idToken: String
    ): Flow<Resource<UserDetailsModel>>

    suspend fun loginWithFacebook(token: String): Flow<Resource<UserDetailsModel>>

    suspend fun registerWithEmailAndPassword(
        name: String, email: String, password: String
    ): Flow<Resource<UserDetailsModel>>
    suspend fun registerEmailAndPasswordWithAPI(registerRequestModel: RegisterRequestModel): Flow<Resource<RegisterResponseModel>>


    suspend fun registerWithGoogle(
        idToken: String
    ): Flow<Resource<UserDetailsModel>>
    suspend fun registerWithGoogleWithAPI(
        idToken: String
    ): Flow<Resource<RegisterResponseModel>>

    suspend fun registerWithFacebook(token: String): Flow<Resource<UserDetailsModel>>

    suspend fun sendUpdatePasswordEmail(email: String): Flow<Resource<String>>

    fun logout()
}