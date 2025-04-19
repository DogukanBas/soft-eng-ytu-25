package com.example.mobile.remote.dtos.auth

import com.example.mobile.model.User.UserType

data class RegisterRequest(
    val personalNo: String,
    val email: String,
    val password: String,
    val userType: String
)
