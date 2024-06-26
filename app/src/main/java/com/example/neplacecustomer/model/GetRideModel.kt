package com.example.neplacecustomer.model

data class GetRideModel(
    val `data`: GetRideData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class GetRideData(
    val airline: String?="",
    val airport_code: Any?="",
    val car_photo: Any?="",
    val child_seat: Int?=0,
    val created_at: String?="",
    val customer_email: String?="",
    val customer_image: String?="",
    val customer_name: String?="",
    val customer_phone: String?="",
    val customer_rating: Int?=0,
    val distance: Double?=0.0,
    val driver_email: String?="",
    val driver_id: Int?=0,
    val driver_image: String?="",
    val driver_name: String?="",
    val driver_phone: String?="",
    val driver_rating: Int?=0,
    val drop_lat: String?="",
    val drop_location: String?="",
    val drop_long: String?="",
    val flight_number: String?="",
    val handicap: Int?=0,
    val id: Int?=0,
    val luggage: Int?=0,
    val make: Any?="",
    val miles: String?="",
    val model: Any?="",
    val no_of_passemger: Int?=0,
    val pickup_city: String?="",
    val pickup_date: String?="",
    val pickup_lat: String?="",
    val pickup_location: String?="",
    val pickup_long: String?="",
    val pickup_time: String?="",
    val price: String?="",
    val ride_status: String?="",
    val ridestatus: String?="",
    val service_type: String?="",
    val updated_at: String?="",
    val user_id: Int?=0,
    val vehicle_id: Any?="",
    val vehicle_type: String?="",
    val vin: Any?="",
    val year: Any?=""
)