package com.example.mobile.remote.api

import com.example.mobile.remote.dtos.Notification.NotificationListResponse
import com.example.mobile.remote.dtos.Notification.NotificationResponse
import retrofit2.Response
import retrofit2.http.GET


interface NotificationService {
    @GET("api/notification/user")
    suspend fun getUserNotification(): Response<List<NotificationResponse>>


}