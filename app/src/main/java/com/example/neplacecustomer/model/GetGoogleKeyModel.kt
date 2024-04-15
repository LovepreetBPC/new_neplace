package com.example.neplacecustomer.model

data class GetGoogleKeyModel(
    val `data`: GoogleKeyData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class GoogleKeyData(
    val google_key: String
)