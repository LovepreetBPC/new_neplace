package com.neplace.customer.model

data class GetPlanModel(
    val `data`: List<PlanData>,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class PlanData(
    val current_plan: Boolean,
    val description: String,
    var isSaved: Boolean,
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
    val type: String
)