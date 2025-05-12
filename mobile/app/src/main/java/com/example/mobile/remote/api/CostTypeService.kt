package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.Accountant.CostTypeBudgetResponseList
import retrofit2.Response
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
        @Query ("maxCost") maxCost: Double,

    ): Response<Unit>
    @POST("api/accountant/cost-types/set-max-cost")
    suspend fun setmaxCost(
        @Query("typeName") costType: String,
        @Query("maxCost") maxCost: Double
    ): Response<Unit>
}
