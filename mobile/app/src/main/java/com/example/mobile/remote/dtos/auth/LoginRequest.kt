package com.example.mobile.remote.dtos.auth

data class LoginRequest (
    val personalNoOrEmail:String,
    val password:String
)