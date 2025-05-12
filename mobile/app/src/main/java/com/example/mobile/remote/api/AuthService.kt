package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.Login.LoginRequest
import com.example.mobile.remote.dtos.Login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


}

