package com.example.neplacecustomer.services

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
import com.example.neplacecustomer.R
import com.example.neplacecustomer.activity.ChatActivity
import com.example.neplacecustomer.activity.DashboardActivity
import com.example.neplacecustomer.activity.DriverSelectionActivity
import com.example.neplacecustomer.activity.NotificationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class NotificationClickReceiver : FirebaseMessagingService() {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "sm"
        private const val NOTIFICATION_CHANNEL_DESC = "NePlace"
    }

    private var url: String? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("Refreshed token:", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e("gtetnotification", "onMessageReceived: ${message.data}")

        url = message.data["url"]
        Log.e("1111", "now11: $url")

        sendNotification(message)
    }

    @SuppressLint("ResourceType")
    private fun sendNotification(remoteMessage: RemoteMessage) {
        var resultIntent: PendingIntent? = null

        url = remoteMessage.data["url"]
        Log.e("1111", "now: $url")

        val notification = remoteMessage.notification

        // Extract custom data
        val data = remoteMessage.data
        val type = data["type"]
        val ride_id = data["ride_id"]

//        val intent = Intent(this, LoginActivity::class.java)
//            .putExtra("urlll", url)
//            .putExtra("type", "notification")

        val intent = when (type) {
            "message" -> Intent(this, ChatActivity::class.java).putExtra("ride_id", ride_id)
            "newride" -> Intent(this, DriverSelectionActivity::class.java).putExtra(
                "ride_id",
                ride_id
            )

            "notification" -> Intent(this, NotificationActivity::class.java)
            else -> Intent(this, DashboardActivity::class.java).putExtra(
                "data",
                remoteMessage.data.toString()
            )
        }

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_DESC
            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        } else {

            notificationBuilder = NotificationCompat.Builder(this, getString(R.string.app_name))
        }

        notificationBuilder.setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["subject"])
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }
}


/*class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        val type = data["type"]
        val ride_id = data["ride_id"]
        Log.e("onMessageReceived", "onMessageReceived: ${remoteMessage.data.toString()}")

        // Show toast indicating the message type
        if (type != null) {
            Log.e("onMessageReceived", "onMessageReceived: ${type.toString()}")

            if (type == "message") {
                openActivity(ChatActivity::class.java, ride_id)
            } else if (type == "newride") {
                openActivity(DriverSelectionActivity::class.java, ride_id)
            } else if (type == "notification") {
                openActivity(NotificationActivity::class.java, ride_id)
            } else {
                openActivity(DashboardActivity::class.java, ride_id)
            }
        }

        showNotification(remoteMessage)
    }

    private fun openActivity(activityClass: Class<*>, ride_id: String?) {
        val intent = Intent(this, activityClass)
        intent.putExtra("trip_id", ride_id)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("onMessageReceived", "onNewToken: $token")
        // Handle token refresh here
        openActivity(NotificationActivity::class.java, null)
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
            .setContentTitle(notification?.title)
            .setContentText(notification?.body)
            .setSmallIcon(R.drawable.img_car)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 0
        notificationManager.notify(notificationId, builder.build())
    }
}*/
