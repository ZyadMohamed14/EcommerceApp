package com.example.ecommerceapp.data.model.auth

data class RegisterRequestModel(
    val email: String,
    val password: String,
    val fullName: String
)

data class RegisterResponseModel (
    val uid: String,
    val email: String,
)