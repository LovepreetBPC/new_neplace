package com.neplace.customer.model

data class RideCancelChargesModel(
    val `data`: RideCancelChargesData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class RideCancelChargesData(
    val alacart: Int,
    val cancel_by: String,
    val cancellation_charges: Int,
    val customer_price: String,
    val date_and_time: String,
    val drop_off_location: String,
    val min_hours: Any,
    val pay_by: String,
    val pickup_date: String,
    val pickup_location: String,
    val service_type: String,
    val trip_id: Int,
    val vehicle_type: String
)