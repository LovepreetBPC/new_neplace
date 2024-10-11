package com.neplace.customer.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.neplace.customer.R
import com.neplace.customer.model.NotificationData
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationAdapter(val data: List<NotificationData>, var handler: DeleteNotificationHandler) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_list_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtName.setText(data[position].title)
        holder.txtTripIdTitle.setText(data[position].body)
        val timestamp = data[position].created_at
        holder.txtTime.setText(convertToTimeAgo(timestamp))

        holder.imgCross.setOnClickListener {
            handler.onClick(data[position].id.toString())
        }


        if (data[position].nf_type.equals("error")) {
            holder.imgNotification.setImageResource(R.mipmap.ic_success_red)

        } else if (data[position].nf_type.equals("warning")) {
            holder.imgNotification.setImageResource(R.mipmap.ic_sucees_yellow)

        } else if (data[position].nf_type.equals("success")) {
            holder.imgNotification.setImageResource(R.mipmap.ic_success_green)

        }


    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var txtName = itemView.findViewById<TextView>(R.id.txtName)
        var txtTime = itemView.findViewById<TextView>(R.id.txtTime)
        var txtTripIdTitle = itemView.findViewById<TextView>(R.id.txtTripIdTitle)
        var imgCross = itemView.findViewById<ImageView>(R.id.imgCross)
        var imgNotification = itemView.findViewById<ImageView>(R.id.imgNotification)
    }

    fun convertToTimeAgo(timestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val utcDate = sdf.parse(timestamp)
        val localSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        localSdf.timeZone = TimeZone.getDefault()
        val localTimeString = localSdf.format(utcDate)
        val date = localSdf.parse(localTimeString)
        val currentTime = Date()

        /*val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        val date = sdf.parse(timestamp)
        val currentTime = Date()*/

        val diffInMillis = abs(currentTime.time - date!!.time)
        Log.d("diffInMillis", "convertToTimeAgo: $diffInMillis")
        Log.d("diffInMillis", "currentTime: $currentTime")
        val diffInSeconds = diffInMillis / 1000
        val diffInMinutes = diffInSeconds / 60
        val diffInHours = diffInMinutes / 60
        val diffInDays = diffInHours / 24
        val diffInWeeks = diffInDays / 7
        val diffInMonths = diffInDays / 30 // Approximating months as 30 days each

        return when {
            diffInMonths >= 1 -> "$diffInMonths months ago"
            diffInWeeks >= 1 -> "$diffInWeeks weeks ago"
            diffInDays >= 1 -> "$diffInDays days ago"
            diffInHours >= 1 -> "$diffInHours hours ago"
            diffInMinutes >= 1 -> "$diffInMinutes minutes ago"
            else -> "$diffInSeconds seconds ago"
        }
    }

    interface DeleteNotificationHandler {
        fun onClick(id: String)

    }
}
