package com.example.mobile.remote.dtos.auth

data class LoginResponse (
    val user: User,
    val token: String,

    )

data class User(
    val id: String,
    val personalNo: String,
    val email: String
)
