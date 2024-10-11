package com.neplace.customer.model

data class Craete_CardToken(
    val card: CardDataa,
    val client_ip: String,
    val created: Int,
    val id: String,
    val livemode: Boolean,
    val `object`: String,
    val type: String,
    val used: Boolean
)

data class CardDataa(
    val address_city: Any,
    val address_country: Any,
    val address_line1: Any,
    val address_line1_check: Any,
    val address_line2: Any,
    val address_state: Any,
    val address_zip: Any,
    val address_zip_check: Any,
    val brand: String,
    val country: String,
    val cvc_check: String,
    val dynamic_last4: Any,
    val exp_month: Int,
    val exp_year: Int,
    val funding: String,
    val id: String,
    val last4: String,
    val name: Any,
    val `object`: String,
    val tokenization_method: Any,
    val wallet: Any
)