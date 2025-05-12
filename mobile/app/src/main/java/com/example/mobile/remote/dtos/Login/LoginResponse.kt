package com.example.mobile.remote.dtos.Login


//todo add surname and name
data class LoginResponse(
    val personalNo: String,
    val email: String,
    val userType: String,
    val accessToken: String,
)
