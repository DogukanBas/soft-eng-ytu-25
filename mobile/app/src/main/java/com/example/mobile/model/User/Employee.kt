package com.example.mobile.model.User

data class Employee(
    val userType :UserType,
    val name :String,
    val surname :String,
    val password:String,
    val email :String,
    val personalNo :String,
    val department :String

)