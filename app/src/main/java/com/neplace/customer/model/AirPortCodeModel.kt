package com.neplace.customer.model

data class AirPortCodeModel(
    val `data`: List<AirPortCodeData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class AirPortCodeData(
    val airport_name: String,
    val code: String
)
