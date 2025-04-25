package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.auth.CostTypeBudgetResponseList
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponse
import com.example.mobile.remote.dtos.auth.DepartmentBudgetResponseList
import com.example.mobile.remote.dtos.auth.LoginRequest
import com.example.mobile.remote.dtos.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CostTypeService {
    @GET("api/accountant/cost-types")
    suspend fun getCostTypeBudget(): Response<CostTypeBudgetResponseList>

    @POST("api/accountant/cost-types/set-initial-budget")
    suspend fun setInitialBudget(
        @Query("typeName") costType: String,
        @Query("initialBudget") initialBudget: Double
    ): Response<Unit>

    @POST("api/accountant/cost-types/set-remaining-budget")
    suspend fun setRemainingBudget(
        @Query("typeName") costType: String,
        @Query("remainingBudget") remainingBudget: Double
    ): Response<Unit>

    @POST("api/accountant/cost-types/reset-budget")
    suspend fun resetRemainingBudget(
        @Query("typeName") costType: String,
    ): Response<Unit>

    @POST("api/accountant/cost-types/add")
    suspend fun addCostTypeBudget(
        @Query("costTypeName") typeName : String,
        @Query ("initialBudget") initialBudget: Double,

    ): Response<Unit>
}
