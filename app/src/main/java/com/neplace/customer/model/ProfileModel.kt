package com.neplace.customer.model

data class ProfileModel(
    val `data`: UserData,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)

data class UserData(
    val security_info: List<Any>,
    val subscription: Boolean,
    val subscriptionDetails: SubscriptionDetails,
    val user: UserProfile,
    val vehicles: List<Any>
)

data class SubscriptionDetails(
    val canceled_at: Any,
    val created_at: String,
    val deleted_at: Any,
    val ends_at: String,
    val id: Int,
    val plan: Plan,
    val plan_id: Int,
    val starts_at: String,
    val transaction_id: String,
    val trial_ends_at: Any,
    val updated_at: String,
    val user_id: Int

)

data class UserProfile(
    val address: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val ntf_count: Int,
    val phone_number: String,
    val rating: Double,
    val stripe_customer_id: String,
    val total_trips: Int,
    val user_id: Int,
    val user_image: String,
    val user_name: String
)

data class Plan(
    val apple_product_id: String,
    val company_id: Any,
    val created_at: Any,
    val deleted_at: Any,
    val description: String,
    val distance_allowed: String,
    val id: Int,
    val image: Any,
    val is_active: Int,
    val name: String,
    val plan_user: String,
    val price: String,
    val slug: String,
    val sort_order: Int,
    val stripe_price_id: String,
    val tagline: String,
    val text_color: Any,
    val trial_interval: String,
    val trial_period: Int,
    val type: String,
    val updated_at: String
)