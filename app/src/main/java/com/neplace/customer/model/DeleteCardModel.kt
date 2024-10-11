package com.neplace.customer.model


data class DeleteCardModel(
    val `data`: Int,
    val message: String,
    val status: Boolean,
    val statusCode: Int?=0
)