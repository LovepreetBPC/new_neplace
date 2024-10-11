package com.neplace.customer.model

data class LoginModel(
    val `data`: OtpData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class OtpData(
    val otp: String,
    val sms: String
)