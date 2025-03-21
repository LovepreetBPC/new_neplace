package com.neplace.customer.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.neplace.customer.R
import com.neplace.customer.activity.ChatActivity
import com.neplace.customer.activity.DashboardActivity
import com.neplace.customer.activity.DriverSelectionActivity
import com.neplace.customer.activity.NotificationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.neplace.customer.NotificationClickReceiver
import com.neplace.customer.activity.AllRidesActivity


//class MyFirebaseMessagingService : FirebaseMessagingService() {

//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//
//        if (remoteMessage.notification != null) {
//            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
//
//            val notificationType = remoteMessage.data["type"] // Assuming you send "type" in data payload
//            Log.d("NotificationType", "Notification Type: $notificationType")
//
//           /* if (notificationType == "newride"){
//                startActivity(Intent(applicationContext, RidesActivity::class.java))
//            }*/
//        }
//    }
//    private fun showNotification(title: String?, message: String?) {
//        val channelId = "default_channel_id"
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Create a notification channel for Android 8.0 and above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.mipmap.app_logo)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//
//        notificationManager.notify(0, notificationBuilder.build())
//    }




//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        remoteMessage.notification?.let {
//            showNotification(it.title, it.body)
//        }
//    }
//
//
//    @SuppressLint("LaunchActivityFromNotification")
//    private fun showNotification(title: String?, message: String?) {
//        val channelId = "default_channel_id"
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Create an intent to open the activity when notification is clicked
//        //val intent = Intent(this, AllRidesActivity::class.java)
//        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        // Wrap intent in a PendingIntent
//        //val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val broadcastIntent = Intent(this, NotificationClickReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//
//        // Create a notification channel for Android 8.0 and above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId, "default_channel_id", NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.mipmap.app_logo)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent) // Set the click action
//
//        notificationManager.notify(0, notificationBuilder.build())
//    }
//}




class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "Firebase registration token: $token")
        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().putString("Token", token).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM Message", "Message received: ${remoteMessage.data}")

        val userInfo = remoteMessage.data
        val notificationType = userInfo["type"] ?: ""
        val appScreenValue = userInfo["app_screen_value"] ?: ""
        Log.d("Notification Data", "app_screen_value: $appScreenValue")

        when (notificationType) {
            "status" -> {
                if (userInfo["status_value"] == "accepted") {
                    sendBroadcast(Intent("reloadTripListTable"))
                }
            }
            "notification" -> {
                val intent = Intent(this, NotificationActivity::class.java)
                showNotification("New Notification", userInfo["message"] ?: "", intent)
            }
            "message" -> {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("ride_id", userInfo["ride_id"])
                showNotification("New Message", userInfo["message"] ?: "", intent)
            }
        }
    }

    private fun showNotification(title: String, message: String, intent: Intent) {
        val channelId = "default_channel"
        val notificationId = System.currentTimeMillis().toInt()

        // Ensure the intent starts a new task when the app is closed
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.app_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId, builder.build())
    }
}