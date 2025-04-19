package com.example.mobile.repositories

import android.util.Log
import com.example.mobile.model.User.UserType
import com.example.mobile.remote.api.AuthService
import com.example.mobile.remote.dtos.auth.LoginRequest
import com.example.mobile.remote.dtos.auth.LoginResponse
import com.example.mobile.remote.dtos.auth.AddUserRequest
import com.example.mobile.remote.dtos.auth.AddUserResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        Log.i("Login initialized", "Login initialized")
        return try {
            val response = authService.login(LoginRequest(username, password))

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }






}