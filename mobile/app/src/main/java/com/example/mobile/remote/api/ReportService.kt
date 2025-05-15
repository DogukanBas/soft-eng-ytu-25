package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.Report.EntityListResponse
import com.example.mobile.remote.dtos.Report.ReportListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {
    @GET ("api/accountant/entities")
    suspend fun getEntities(@Query("query") type:String): Response<EntityListResponse>
    @GET("api/accountant/stats")
    suspend fun getReport(@Query("type") type:String, @Query("id") id:String): Response<ReportListResponse>
}