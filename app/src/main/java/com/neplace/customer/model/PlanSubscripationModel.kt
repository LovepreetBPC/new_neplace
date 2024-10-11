package com.neplace.customer.model

data class PlanSubscripationModel(
    val `data`: PlanSubscripationData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class PlanSubscripationData(
    val canceled_at: Any,
    val created_at: String,
    val deleted_at: Any,
    val ends_at: String,
    val id: Int,
    val plan_id: String,
    val starts_at: String,
    val transaction_id: String,
    val trial_ends_at: Any,
    val updated_at: String,
    val user_id: Int
)