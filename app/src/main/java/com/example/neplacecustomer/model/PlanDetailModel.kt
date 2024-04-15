package com.example.neplacecustomer.model

data class PlanDetailModel(
    val `data`: PlanDetailData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class PlanDetailData(
    val created_at: Any,
    val deleted_at: Any,
    val description: List<String>,
    val id: Int,
    val image: Any,
    val is_active: Int,
    val name: String,
    val price: String,
    val slug: String,
    val sort_order: Int,
    val tagline: String,
    val text_color: Any,
    val trial_interval: String,
    val trial_period: Int,
    val type: String,
    val updated_at: Any
)