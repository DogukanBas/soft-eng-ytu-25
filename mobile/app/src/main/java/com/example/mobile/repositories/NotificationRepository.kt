package com.example.mobile.repositories

import com.example.mobile.remote.api.NotificationService
import com.example.mobile.remote.dtos.Notification.NotificationResponse
import javax.inject.Inject


class NotificationRepository @Inject constructor(
    private val notificationService: NotificationService
) {
    suspend fun getUserNotification() : Result<List<NotificationResponse>> {
        return try {
            val response = notificationService.getUserNotification()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Geting Notifcations Failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}