package com.example.mobile.repositories

import android.util.Log
import com.example.mobile.model.User.Employee
import com.example.mobile.remote.api.AdminService
import com.example.mobile.remote.dtos.auth.AddUserResponse
import com.example.mobile.utils.toDto
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val adminService: AdminService
){
    suspend fun addUser(employee: Employee) : Result<AddUserResponse> {
        Log.i("AddUser initialized", "Repository ")
        return try {
            Log.i("AddUser initialized", "Repository 2")
            val response = adminService.addUser(employee.toDto())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("AddUser failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
