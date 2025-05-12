package com.example.mobile.di

import com.example.mobile.remote.RetrofitClient
import com.example.mobile.remote.api.AdminService
import com.example.mobile.remote.api.AuthService
import com.example.mobile.remote.api.CostTypeService
import com.example.mobile.remote.api.DepartmentService
import com.example.mobile.remote.api.TicketService
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
    @Provides
    @Singleton
    fun provideCostTypeService(): CostTypeService {
        return RetrofitClient.costTypeService
    }

    @Provides
    @Singleton
    fun provideTicketService(): TicketService {
        return RetrofitClient.ticketService
    }

    @Provides
    @Singleton
    fun provideNotificationService(): com.example.mobile.remote.api.NotificationService {
        return RetrofitClient.notificationService
    }

}