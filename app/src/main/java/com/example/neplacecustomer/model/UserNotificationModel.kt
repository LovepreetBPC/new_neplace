package com.example.neplacecustomer.model

data class UserNotificationModel(
    val message: String,
    val status: Boolean,
    val statusCode: Int,
    val `data`: List<NotificationData>
)


data class NotificationData(
    val body: String,
    val created_at: String,
    val id: Int,
    val is_read: Int,
    val nf_type: String,
    val title: String,
    val type: Any,
    val updated_at: String,
    val user_id: Int
)