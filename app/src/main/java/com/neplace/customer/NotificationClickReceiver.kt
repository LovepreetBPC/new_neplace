package com.neplace.customer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.neplace.customer.activity.AllRidesActivity

class NotificationClickReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val newIntent = Intent(context, AllRidesActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(newIntent)
    }
}