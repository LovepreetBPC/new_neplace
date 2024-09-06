package com.example.neplacecustomer.model

data class StripePaymentModel(
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class StripePaymentData(
    val amount: String,
    val message: String,
    val ride_id: Int,
    val status: String,
    val txt_id: String,
    val user_id: String
)