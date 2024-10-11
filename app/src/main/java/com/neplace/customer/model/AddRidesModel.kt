package com.neplace.customer.model

data class AddRidesModel(
    val `data`: RideData,
    val message: String?="",
    val status: Boolean,
    val statusCode: Int
)

data class RideData(
    val airline: String,
    val child_seat: String,
    val created_at: String,
    val driver_id: String,
    val drop_lat: String,
    val drop_location: String,
    val drop_long: String,
    val flight_number: String,
    val handicap: String,
    val id: Int,
    val miles: Double,
    val no_of_passemger: String,
    val pickup_city: String,
    val pickup_date: String,
    val pickup_lat: String,
    val pickup_location: String,
    val pickup_long: String,
    val pickup_time: String,
    val price: Double,
    val ride_status: String,
    val service_type: String,
    val updated_at: String,
    val user_id: Int,
    val vehicle_type: String,
    )