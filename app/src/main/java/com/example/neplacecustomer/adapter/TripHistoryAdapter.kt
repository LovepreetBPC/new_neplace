package com.example.neplacecustomer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neplacecustomer.R
import com.example.neplacecustomer.activity.ChatActivity
import com.example.neplacecustomer.activity.DriverSelectionActivity
import com.example.neplacecustomer.activity.EditScheduleRideActivity
import com.example.neplacecustomer.model.RideHistoryData
import com.example.neplacecustomer.common.Constant
import java.text.SimpleDateFormat
import java.util.Locale


class TripHistoryAdapter(val context: Context, val data: List<RideHistoryData>, val type: String) :
    RecyclerView.Adapter<TripHistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_ride_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        if (type.equals("upComing")) {
//            holder.txtWating.visibility = View.VISIBLE
//            holder.view.visibility = View.VISIBLE
//        } else {
//            holder.txtWating.visibility = View.GONE
//            holder.view.visibility = View.GONE
//        }

        val status = data[position].status
        when (status) {
            "active" -> {
                holder.relativeDriverProfile.visibility = View.GONE
                holder.txtWating.visibility = View.VISIBLE
                holder.imgEdit.visibility = View.VISIBLE
                holder.icMessage.visibility = View.GONE
                holder.linearRating.visibility = View.GONE
            }
            "Active" -> {
                holder.relativeDriverProfile.visibility = View.GONE
                holder.txtWating.visibility = View.VISIBLE
                holder.icMessage.visibility = View.GONE
                holder.imgEdit.visibility = View.VISIBLE
                holder.linearRating.visibility = View.GONE
            }
            "completed" -> {
                holder.relativeDriverProfile.visibility = View.VISIBLE
                holder.txtWating.visibility = View.GONE
                holder.imgEdit.visibility = View.GONE
                holder.icMessage.visibility = View.GONE
                holder.linearRating.visibility = View.VISIBLE
            }
            else -> {
                holder.relativeDriverProfile.visibility = View.GONE
                holder.txtWating.visibility = View.VISIBLE
                holder.imgEdit.visibility = View.GONE
                holder.icMessage.visibility = View.VISIBLE
            }
        }
         if (data[position].user.user_id.toString().isNullOrEmpty()){
            holder.relativeDriverProfile.visibility = View.GONE
            holder.txtWating.visibility = View.VISIBLE
        } else if (data[position].driver_name == null){
            holder.relativeDriverProfile.visibility = View.GONE
            holder.txtWating.visibility = View.VISIBLE
        } else if (data[position].driver_name != null){
            holder.relativeDriverProfile.visibility = View.VISIBLE
            holder.txtWating.visibility = View.GONE
        }
        else {
            holder.relativeDriverProfile.visibility = View.VISIBLE
            holder.txtWating.visibility = View.GONE


        }
        if (data[position].driver_name != null && data[position].driver_name.isNotEmpty()) {
            holder.txtDriverName.text = data[position].driver_name.toString()
        }
        if (data[position].vehicle_type != null) {
            holder.txtCarName.text = data[position].vehicle_type.toString()
        }


        if (data[position].driver_image != null) {
            Glide.with(context).load(Constant.BASEURL + data[position].driver_image)
                .placeholder(R.mipmap.img_place_holder)
                .error(R.mipmap.img_place_holder)
                .into(holder.imgProfile)
        }

        holder.icMessage.setOnClickListener {


            if (data[position].driver_phone != null) {
//                makePhoneCall(data[position].driver_phone.toString())
                context.startActivity(Intent(context, ChatActivity::class.java)
                    .putExtra("driver_id",data[position].driver_id.toString())
                    .putExtra("trip_id", data[position].trip_id.toString())
                    .putExtra("driver_phoneNumber", data[position].driver_phone.toString())
                    .putExtra("driver_name",  data[position].driver_name.toString())
                    .putExtra("driver_image", data[position].driver_image.toString())

                )

            } else {
//                makePhoneCall("")
                context.startActivity(Intent(context, ChatActivity::class.java)
                    .putExtra("driver_id",data[position].driver_id.toString())
                    .putExtra("trip_id", data[position].trip_id.toString())
                    .putExtra("driver_phoneNumber", "")
                    .putExtra("driver_name",  data[position].driver_name.toString())
                    .putExtra("driver_image", data[position].driver_image.toString())
                )
            }
        }



        holder.imgEdit.setOnClickListener {
            context.startActivity(Intent(context,EditScheduleRideActivity::class.java).putExtra("trip_id",data[position].trip_id.toString()))
        }


        if (data[position].ride_rating.toInt() < 1) {
            Log.e("ride_status", "onBindViewHolder: " + data[position].status)

            holder.linearRating.visibility = View.GONE
            holder.txtRating.visibility = View.GONE
            holder.rating.visibility = View.GONE
        } else {
            Log.e("ride_status", "onBindViewHolder else: " + data[position].status)
            holder.rating.visibility = View.VISIBLE
            holder.txtRating.visibility = View.VISIBLE
            holder.linearRating.visibility = View.VISIBLE


        }




            holder.itemView.setOnClickListener {
                if (data[position].ratting_given){
                    Log.e("ratingAdd", "onBindViewHolder: No Need for ratting")
                }
                else{
                    context.startActivity(Intent(context, DriverSelectionActivity::class.java)
                    .putExtra("drop_lat", data[position].drop_lat)
                    .putExtra("drop_long", data[position].drop_long)
                    .putExtra("pickup_lat", data[position].pickup_lat)
                    .putExtra("pickup_long", data[position].pickup_long)
                    .putExtra("pickup_location", data[position].pickup_location)
                    .putExtra("drop_location", data[position].drop_location)
                    .putExtra("vehicle_type", data[position].vehicle_type.toString())
                    .putExtra("id", data[position].trip_id.toString())
                    .putExtra("user_id", data[position].user.user_id.toString())
                    .putExtra("ride_status", data[position].ridestatus)
                )
            }
        }




        val inputDateTime = data[position].pickup_date.toString() + ", " + data[position].pickup_time.toString()

        holder.txtTime.text = convertDateTime(inputDateTime)
//        holder.txtTime.text = inputDateTime.toString()
        holder.txtStatusName.text = data[position].status
        holder.txtCar.text = data[position].vehicle_type.toString() + " , " + data[position].no_of_passemger + " Passenger/s, "+ data[position].luggage + " Luggage"
        holder.txtPickUpAddress.text = data[position].pickup_location
        holder.txtDropAddress.text = data[position].drop_location
        holder.txtAmount.text = "$" + data[position].price.toString()
        val rating = data[position].ride_rating.toFloat()
        holder.txtRating.text = rating.toString()
        holder.rating.rating = data[position].ride_rating.toFloat()
        holder.txtMiles.text = data[position].miles.toString() + "\nMiles"
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var txtStatusName: TextView = itemView.findViewById(R.id.txtStatusName)
        var txtTime: TextView = itemView.findViewById(R.id.txtTime)
        var txtPickUpAddress: TextView = itemView.findViewById(R.id.txtPickUpAddress)
        var txtDropAddress: TextView = itemView.findViewById(R.id.txtDropAddress)
        var txtAmount: TextView = itemView.findViewById<TextView>(R.id.txtAmount)
        var txtMiles: TextView = itemView.findViewById<TextView>(R.id.txtMiles)
        var txtCar: TextView = itemView.findViewById<TextView>(R.id.txtCar)
        var txtWating: TextView = itemView.findViewById<TextView>(R.id.txtWating)
        var view: View = itemView.findViewById<View>(R.id.view)
        var relativeDriverProfile: View = itemView.findViewById<View>(R.id.relativeDriverProfile)
        var txtDriverName: TextView = itemView.findViewById<TextView>(R.id.txtDriverName)
        var txtCarName: TextView = itemView.findViewById<TextView>(R.id.txtCarName)
        var imgProfile: ImageView = itemView.findViewById<ImageView>(R.id.imgProfile)
        var icMessage: ImageView = itemView.findViewById<ImageView>(R.id.icMessage)
        var imgEdit: ImageView = itemView.findViewById<ImageView>(R.id.imgEdit)
        var relativePrice: RelativeLayout = itemView.findViewById(R.id.relativePrice)
        var linearRating: LinearLayout = itemView.findViewById(R.id.linearRating)
        var rating: RatingBar = itemView.findViewById(R.id.rating)
        var txtRating: TextView = itemView.findViewById(R.id.txtRating)


    }

    private fun makePhoneCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$phoneNumber")

        if (context.packageManager.resolveActivity(callIntent, 0) != null) {
            context.startActivity(callIntent)
        } else {
            Toast.makeText(context, "No phone app found", Toast.LENGTH_SHORT).show()
        }
    }

    fun convertDateTime(inputDateTime: String): String {
        try {
            // Define the input and output date formats
            val inputFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEEE, MMMM d, yyyy, HH:mm", Locale.getDefault())

            // Parse the input date and time
            val date = inputFormat.parse(inputDateTime)

            // Format the date and time in the desired format
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid Date/Time"
        }
    }

}