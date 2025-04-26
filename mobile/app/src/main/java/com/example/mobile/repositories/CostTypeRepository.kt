package com.example.mobile.remote.repository

import RetrofitClient.costTypeService
import android.util.Log
import com.example.mobile.remote.dtos.auth.CostTypeBudgetResponse
import javax.inject.Inject

class CostTypeRepository @Inject constructor() {
    companion object {
        private const val TAG = "CostTypeRepository"
    }
    suspend fun getCostTypeBudgets(): Result<List<CostTypeBudgetResponse>> {
        Log.i(TAG, "Get cost type called")
        return try {
            val response = costTypeService.getCostTypeBudget()

            if (response.isSuccessful && response.body() != null) {
                Log.i(TAG, "Get cost type successful, ${response.body()!!.costTypes}" )
                Result.success(response.body()!!.costTypes)
            } else {
                Log.i(TAG, "Get cost type failed: ${response.message()}")
                Result.failure(Exception("Get cost type failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setInitialBudget(costType:String, newInitialBudget:Double): Result<Unit> {
        Log.i(TAG, "Set initial budget called")
        return try {
            val response = costTypeService.setInitialBudget(costType, newInitialBudget)
            if (response.isSuccessful) {
                Log.i(TAG, "Set initial budget successful")
                Result.success(response.body()!!)
            } else {
                Log.i(TAG, "Set initial budget failed: ${response.message()}")
                Result.failure(Exception("Set initial budget failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun setRemainingBudget(costType: String, budgetDouble: Double): Result<Unit> {
        Log.i(TAG, "Set remaining budget called")
        return try {
            val response = costTypeService.setRemainingBudget(costType, budgetDouble)
            if (response.isSuccessful) {
                Log.i(TAG, "Set remaining budget successful")
                Result.success(response.body()!!)
            } else {
                Log.i(TAG, "Set remaining budget failed: ${response.message()}")
                Result.failure(Exception("Set remaining budget failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun resetRemainingBudget(costType: String): Result<Unit> {
        Log.i(TAG, "Reset remaining budget called")
        return try {
            val response = costTypeService.resetRemainingBudget(costType)
            if (response.isSuccessful) {
                Log.i(TAG, "Reset remaining budget successful")
                Result.success(response.body()!!)
            } else {
                Log.i(TAG, "Reset remaining budget failed: ${response.message()}")
                Result.failure(Exception("Reset remaining budget failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun addCostTypeBudget(costType: String, initialBudget: Double): Result<Unit> {
        Log.i(TAG, "Add cost type budget called")
         try {
            val response = costTypeService.addCostTypeBudget(costType,initialBudget)
            if (response.isSuccessful) {
                //Log.i(TAG, "Add cost type budget successful")
                return Result.success(response.body()!!)

            } else {
                //Log.i(TAG, "Add cost type budget failed: ${response.message()}")
                return Result.failure(Exception("Add cost type budget failed: ${response.headers().get("message")}"))
            }
        } catch (e: Exception) {
             return Result.failure(e)
        }
    }
    suspend fun setmaxCost(costType: String, maxCost: Double): Result<Unit> {
        Log.i(TAG, "Set max budget called")
        return try {
            val response = costTypeService.setmaxCost(costType, maxCost)
            if (response.isSuccessful) {
                Log.i(TAG, "Set max budget successful")
                Result.success(response.body()!!)
            } else {
                Log.i(TAG, "Set max budget failed: ${response.headers().get("message")}")
                Result.failure(Exception("Set max budget failed: ${response.headers().get("message")}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}