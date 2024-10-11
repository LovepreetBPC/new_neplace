package com.neplace.customer.model

data class ChooseVehicleModel(
    val `data`: ChooseVehicleData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class ChooseVehicleData(
    val pickup_lat: String,
    val pickup_long: String,
    val ride_lat: String,
    val ride_long: String,
    val vehicle: Vehicle
)

data class Vehicle(
    val car_photo: String,
    val model: String
)