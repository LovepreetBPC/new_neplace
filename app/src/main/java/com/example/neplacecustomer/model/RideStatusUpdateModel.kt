package com.example.neplacecustomer.model

data class RideStatusUpdateModel(
    val `data`: RideStatusUpdateData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class RideStatusUpdateData(
    val created_at: String,
    val driver_id: Int,
    val id: Int,
    val ride_id: String,
    val status: String,
    val updated_at: String
)