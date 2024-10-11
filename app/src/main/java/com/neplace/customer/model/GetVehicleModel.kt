package com.neplace.customer.model

data class GetVehicleModel(
    val `data`: List<GetVehicleData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class GetVehicleData(
    val base_price: String,
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val seat: String,
    val updated_at: String
)