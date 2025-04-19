package com.example.mobile.remote.dtos.auth

data class AddUserRequest(
    val name:String,
    val surname:String,
    val personalNo: String,
    val email: String,
    val password: String,
    val userType: String
)
