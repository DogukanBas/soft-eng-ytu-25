package com.example.mobile.remote.dtos.Admin

data class AddUserRequest(
    val name:String,
    val surname:String,
    val personalNo: String,
    val email: String,
    val password: String,
    val userType: String,
    val deptName: String
)
