package com.example.mobile.repositories
import android.util.Log
import com.example.mobile.remote.api.DepartmentService
import com.example.mobile.remote.dtos.Accountant.DepartmentBudgetResponse
import javax.inject.Inject


class DepartmentRepository @Inject constructor(
    private val deptService: DepartmentService
) {
    companion object {
        const val TAG = "DepartmentRepository"
    }
    suspend fun getDepartmentBudget(): Result<List<DepartmentBudgetResponse>> {
        Log.i(TAG, "Get department called")
        return try {
            val response = deptService.getDepartmentBudget()

            if (response.isSuccessful && response.body() != null) {
                Log.i(TAG, "Get department successful")
                Result.success(response.body()!!.departments)
            } else {
                Log.i(TAG, "Get department failed: ${response.message()}")
                Result.failure(Exception("Get department failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun setInitialBudget(deptName:String, newInitialBudget:Double): Result<Unit> {
        Log.i(TAG, "Set initial budget called")
        return try {
            val response = deptService.setInitialBudget(deptName, newInitialBudget)
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
    suspend fun setRemainingBudget(department: String, budgetDouble: Double): Result<Unit> {
        Log.i(TAG, "Set remaining budget called")
        return try {
            val response = deptService.setRemainingBudget(department, budgetDouble)
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
    suspend fun resetRemainingBudget(department: String): Result<Unit> {
        Log.i(TAG, "Reset remaining budget called")
        return try {
            val response = deptService.resetRemainingBudget(department)
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
}