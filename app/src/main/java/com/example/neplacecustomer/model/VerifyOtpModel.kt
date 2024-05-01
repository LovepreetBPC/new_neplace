package com.example.neplacecustomer.model

data class VerifyOtpModel(
    val `data`: Data,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class Data(
    val is_blocked: Boolean,
    val token: String?="",
    val user: User
)

data class User(
    val address: Any?="",
    val apple_id: Any?="",
    val city: String?="",
    val created_at: String?="",
    val device_token: Any?="",
    val device_type: Any?="",
    val email: String?="",
    val email_verified_at: Any?="",
    val first_name: String?="",
    val google_id: Any?="",
    val id: Int?=0,
    val image: Any?="",
    val last_name: String?="",
    val name: String?="",
    val personal_id_card: String?="",
    val personal_id_card_backt_image: Any?="",
    val personal_id_card_front_image: Any?="",
    val phone: String?="",
    val role_id: Int?=0,
    val status: Int?=0,
    val type: Any?="",
    val updated_at: String?=""
)