package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.auth.AddUserRequest
import com.example.mobile.remote.dtos.auth.AddUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AdminService {

    @POST("api/auth/register")
    suspend fun addUser(@Body request: AddUserRequest): Response<AddUserResponse>

}