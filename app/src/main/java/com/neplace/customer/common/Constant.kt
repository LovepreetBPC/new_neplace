package com.neplace.customer.common

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

object Constant {

//    var BASEURL: String = "http://18.246.71.89/"
    var BASEURL: String = "https://app.goneplace.app/"

    val USERTYPE: String = "UserType"
    val TOKEN: String = "Token"
    val DEVICETOKEN: String = "DeviceToken"
    val USERID: String = "userid"
    val STRIPE_ID: String = "stripeId"
    val EMAIL: String = "email"
    val PHONE_NUMBER: String = "phone_number"
    val USERNAME: String = "UserName"
    val PlanID: String = "PlanID"
    val USERIMAGE: String = "UserImage"


    val User_Stripe_Id="User_Stripe_Id"
    val CardName="card_name"
    val CardNumber="card_number"
    val CardMonth="card_month"
    val CardYear="card_year"
    val CardCvv="card_cvv"
    val CardSave="card_saved"



    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd,HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())

        val date = inputFormat.parse(inputDate)

        Log.e("getData098765" ,"formatDate: " + inputDate + "-------------"+outputFormat.format(date))
        return outputFormat.format(date!!)
    }

}