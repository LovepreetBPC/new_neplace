package com.example.neplacecustomer.model

data class GoogleAPiKeyModel(
    val `data`: ApiKeyData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class ApiKeyData(
    val google_key: String
)