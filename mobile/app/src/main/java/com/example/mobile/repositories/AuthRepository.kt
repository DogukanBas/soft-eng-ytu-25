package com.example.mobile.repositories
import android.util.Log
import com.example.mobile.remote.api.AuthService
import com.example.mobile.remote.dtos.auth.LoginRequest
import com.example.mobile.remote.dtos.auth.LoginResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    companion object {
        const val TAG = "AuthRepository"
    }
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        Log.i(TAG, "login called")
        return try {
            val response = authService.login(LoginRequest(username, password))

            if (response.isSuccessful && response.body() != null) {
                Log.i(TAG, "Login successful")
                Result.success(response.body()!!)
            } else {
                Log.i(TAG, "Login failed: ${response.body()!!}")
                Result.failure(Exception("Login failed: ${response.headers()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }






}