package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.Admin.AddDepartmentRequest
import com.example.mobile.remote.dtos.Admin.AddUserRequest
import com.example.mobile.remote.dtos.Admin.AddUserResponse
import com.example.mobile.remote.dtos.DepartmentsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AdminService {
    @GET("api/admin/departments")
    suspend fun getDepartments(): Response<DepartmentsResponse>
    @POST("api/admin/add-employee")
    suspend fun addUser(@Body request: AddUserRequest): Response<AddUserResponse>

    @POST("api/admin/add-department")
    suspend fun addDepartment(@Body request: AddDepartmentRequest): Response<Unit>

}