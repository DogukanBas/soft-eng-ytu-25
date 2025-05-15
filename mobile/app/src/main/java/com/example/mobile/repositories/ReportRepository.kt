package com.example.mobile.repositories

import com.example.mobile.remote.api.ReportService
import com.example.mobile.remote.dtos.Report.EntityListResponse
import com.example.mobile.remote.dtos.Report.EntityResponse
import com.example.mobile.remote.dtos.Report.ReportListResponse
import com.example.mobile.remote.dtos.Report.ReportResponse
import javax.inject.Inject

class ReportRepository @Inject constructor(
    private val notificationService: ReportService
) {
    suspend fun getEntities(type: String): Result<List<EntityResponse>> {
        return try {
            val response = notificationService.getEntities(type)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.entities)
            } else {
                Result.failure(Exception("Getting Report Failed: ${response.headers().get("message")}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReport(type: String,id: String): Result<List<ReportResponse>> {
        return try {
            val response = notificationService.getReport(type,id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.reports)
            } else {
                Result.failure(Exception("Getting Report Failed: ${response.headers().get("message")}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // Add other repository methods as needed

}