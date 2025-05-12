package com.example.mobile.remote

import com.example.mobile.remote.api.AdminService
import com.example.mobile.remote.api.AuthService
import com.example.mobile.remote.api.CostTypeService
import com.example.mobile.remote.api.DepartmentService
import com.example.mobile.remote.api.TicketService
import com.example.mobile.utils.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var baseUrl = "http://10.0.2.2:8080/"
    private var retrofit: Retrofit? = null

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(AuthInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun updateBaseUrl(ip: String) {
        baseUrl = "http://$ip:8080/"
        retrofit = null
    }

    private fun getRetrofit(): Retrofit {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().also { retrofit = it }
    }

    val authService: AuthService by lazy {
        getRetrofit().create(AuthService::class.java)
    }
    val adminService: AdminService by lazy {
        getRetrofit().create(AdminService::class.java)
    }
    val departmentService: DepartmentService by lazy {
        getRetrofit().create(DepartmentService::class.java)
    }
    val costTypeService: CostTypeService by lazy {
        getRetrofit().create(CostTypeService::class.java)
    }
    val ticketService: TicketService by lazy {
        getRetrofit().create(TicketService::class.java)
    }

}