package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponse
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponseList
import com.example.mobile.remote.dtos.auth.LoginRequest
import com.example.mobile.remote.dtos.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DepartmentService {
    @GET("api/accountant/departments")
    suspend fun getDepartmentBudget(): Response<DepartmentBudgetResponseList>

    @POST("api/accountant/departments/set-initial-budget")
    suspend fun setInitialBudget(
        @Query("deptName") deptName: String,
        @Query("initialBudget") initialBudget: Double
    ): Response<Unit>

    @POST("api/accountant/departments/set-remaining-budget")
    suspend fun setRemainingBudget(
        @Query("deptName") department: String,
        @Query("remainingBudget") remainingBudget: Double
    ): Response<Unit>

    @POST("api/accountant/departments/reset-budget")
    suspend fun resetRemainingBudget(
        @Query("deptName") department: String,
    ): Response<Unit>
}
