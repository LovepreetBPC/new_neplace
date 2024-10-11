package com.neplace.customer.model

data class AirlineModel(
    val `data`: List<AirlineData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class AirlineData(
    val airline_name: String,
    val code: String
)