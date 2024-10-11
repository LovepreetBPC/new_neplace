package com.neplace.customer


data class ChatModel (
    val `data`: List<Message>,
    val end_datetime: String,
    val list_id: Int,
)

data class Message (
    val message: String,
    val isFromCurrentUser: Boolean

//    val product_name: String,
//    val quantity: Int,
//    val status: String,
) {

}