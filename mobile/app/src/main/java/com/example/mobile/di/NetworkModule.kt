package com.example.mobile.di

import com.example.mobile.remote.api.AdminService
import com.example.mobile.remote.api.AuthService
import com.example.mobile.remote.api.DepartmentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return RetrofitClient.authService
    }

    @Provides
    @Singleton
    fun provideAdminService(): AdminService {
        return RetrofitClient.adminService
    }

    @Provides
    @Singleton
    fun provideDepartmentService(): DepartmentService {
        return RetrofitClient.departmentService
    }
}