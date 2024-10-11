package com.neplace.customer.model

data class SocialLoginModel(
    val `data`: SocialLoginData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class SocialLoginData(
    val is_blocked: Boolean,
    val name: String,
    val token: String
)