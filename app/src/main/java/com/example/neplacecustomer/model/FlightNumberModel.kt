package com.example.neplacecustomer.model

data class FlightNumberModel(
    val `data`: List<FlightNumberData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class FlightNumberData(
    val eta: String?="",
    val flight_number: String? = ""
)