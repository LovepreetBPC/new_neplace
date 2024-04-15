package com.example.neplacecustomer.model

data class AddCardModel(
    val `data`: List<Any>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)