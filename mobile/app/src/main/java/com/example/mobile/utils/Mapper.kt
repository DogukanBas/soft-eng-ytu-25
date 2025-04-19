package com.example.mobile.utils

import com.example.mobile.model.User.Employee
import com.example.mobile.remote.dtos.auth.AddUserRequest

fun Employee.toDto(): AddUserRequest {
    return AddUserRequest(
        name = this.name,
        surname = this.surname,
        personalNo = this.personalNo,
        email = this.email,
        password = this.password,
        userType = this.userType.toString()
    )
}
