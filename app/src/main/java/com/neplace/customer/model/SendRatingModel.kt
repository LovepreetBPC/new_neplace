package com.neplace.customer.model

data class SendRatingModel(
    val `data`: RatingData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class RatingData(
    val created_at: String,
    val description: String,
    val id: Int,
    val rated_by_id: Int,
    val rated_user_id: String,
    val rating: String,
    val ride_id: String,
    val updated_at: String
)