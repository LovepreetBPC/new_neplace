package com.example.neplacecustomer.model

data class GetCards(
    val `data`: List<GetCardsData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class GetCardsData(
    val card_number: String,
    val cardid: String,
    val created_at: String,
    val default_card: Int,
    val id: Int,
    val tokenid: String,
    val updated_at: String,
    val userid: Int
)