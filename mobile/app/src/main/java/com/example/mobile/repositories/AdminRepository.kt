package com.example.mobile.repositories

import android.util.Log
import com.example.mobile.model.User.Employee
import com.example.mobile.remote.api.AdminService
import com.example.mobile.remote.dtos.Admin.AddDepartmentRequest
import com.example.mobile.remote.dtos.Admin.AddUserResponse
import com.example.mobile.utils.toDto
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val adminService: AdminService

){
    companion object{
        val TAG = "AdminRepository"
    }
    suspend fun getDepartments(): Result<List<String>> {
        Log.i(TAG, "getDepartments called")
        return try {
            val response = adminService.getDepartments()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.departments)
            } else {
                Result.failure(Exception("GetDepartments failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun addUser(employee: Employee) : Result<AddUserResponse> {
        Log.i(TAG, "addUser called")
        return try {
            val response = adminService.addUser(employee.toDto())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {

                Result.failure(Exception(response.headers()["message"]))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun addDepartment(departmentName: String) : Result<Unit> {
        Log.i(TAG, "add department called")
        return try {
            val response = adminService.addDepartment(AddDepartmentRequest(departmentName))
            if (response.isSuccessful && response.body() != null) {
                Log.i(TAG, "add department successful")
                Result.success(response.body()!!)
            } else {
                Log.i(TAG, "add department failed: ${response.message()}")
                Result.failure(Exception(response.headers()["message"]))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
