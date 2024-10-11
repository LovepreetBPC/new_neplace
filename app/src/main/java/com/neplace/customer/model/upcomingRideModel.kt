package com.neplace.customer.model

data class upcomingRideModel(
    val `data`: List<upcomingRideData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class upcomingRideData(
    val airline: String,
    val airport_code: String,
    val child_seat: Int,
    val customer_image: Any,
    val customer_name: Any,
    val customer_phone: Any,
    val driver_email: Any,
    val driver_image: Any,
    val driver_name: Any,
    val driver_phone: Any,
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
    val ridestatus: Any,
    val service_type: String,
    val status: String,
    val trip_id: Int,
    val user: User,
    val vehicle_type: String,
    val vin: Any,
    val year: Any
)

data class upcomingRideUser(
    val email: String,
    val fist_name: Any,
    val last_name: String,
    val user_id: Int,
    val user_image: String,
    val user_name: String
)