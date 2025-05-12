package com.example.mobile.remote.dtos.Notification

data class NotificationResponse(
    val id :Int,
    val type:String,
    val message: String,
    val createdAt: String
)
