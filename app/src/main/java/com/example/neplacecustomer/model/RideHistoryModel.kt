package com.example.neplacecustomer.model

data class RideHistoryModel(
    val `data`: List<RideHistoryData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class RideHistoryData(
    val airline: String,
    val airport_code: Any,
    val child_seat: Int,
    val customer_image: String,
    val customer_name: String,
    val customer_phone: String,
    val driver_email: String,
    val driver_image: String,
    val driver_name: String,
    val driver_phone: Any,
    val driver_id: String?="",
    val drop_lat: String,
    val drop_location: String,
    val drop_long: String,
    val flight_number: String,
    val handicap: Int,
    val luggage: Int,
    val make: Any,
    val miles: String,
    val no_of_passemger: Int,
    val pickup_city: String,
    val pickup_date: String,
    val pickup_lat: String,
    val pickup_location: String,
    val pickup_long: String,
    val pickup_time: String,
    val price: String,
    val ridestatus: String,
    val service_type: String,
    val status: String,
    val trip_id: Int,
    val user: RideHistoryUser,
    val vehicle_type: Any,
    val vin: Any,
    val year: Any,
    val alacart: Int,
    val driver_price: Int,
    val min_hours: Any,
    val ratting_given: Boolean,
    val ride_rating: Int,
    val vehicle_seat: String,
)

data class RideHistoryUser(
    val email: String,
    val fist_name: Any,
    val last_name: String,
    val rating: Double,
    val user_id: Int,
    val user_image: String,
    val user_name: String
)