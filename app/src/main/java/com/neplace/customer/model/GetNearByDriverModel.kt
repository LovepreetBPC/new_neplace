package com.neplace.customer.model

data class GetNearByDriverModel(
    val `data`: List<GetNearByDriverData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class GetNearByDriverData(
    val car_name: String,
    val driver_name: String,
    val id: Int,
    val km_away: Double,
    val price: Double,
    val profile_pic: String,
    val rating: Int
)