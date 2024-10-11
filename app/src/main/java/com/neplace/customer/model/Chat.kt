package com.neplace.customer.model



data class Chat(var senderID: String = "", var message: String = "", var senderName: String = "", var created: Long = 0) {
    // No-argument constructor
    constructor() : this("", "", "", 0)
}

//data class Chat(
//    var senderId: String = "",
//    var message: String = "",
//    var senderName: String = "",
//    var create: Long
//
//
//)

