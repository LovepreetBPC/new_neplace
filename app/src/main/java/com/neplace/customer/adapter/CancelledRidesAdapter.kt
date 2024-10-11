package com.neplace.customer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neplace.customer.R
import com.neplace.customer.model.RideHistoryData
import com.neplace.customer.common.Constant
import java.text.SimpleDateFormat
import java.util.Locale


class CancelledRidesAdapter(val context: Context, val data: List<RideHistoryData>, val type: String) :
    RecyclerView.Adapter<CancelledRidesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_ride_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val status = data[position].status
        if (status == "active") {
            holder.relativeDriverProfile.visibility = View.GONE
            holder.txtWating.visibility = View.VISIBLE

        } else if (data[position].user.user_id.toString().isNullOrEmpty()){
            holder.relativeDriverProfile.visibility = View.GONE
            holder.txtWating.visibility = View.VISIBLE
        } else if (data[position].driver_name == null){
            holder.relativeDriverProfile.visibility = View.GONE
            holder.txtWating.visibility = View.VISIBLE
        } else if (data[position].driver_name != null){
            holder.relativeDriverProfile.visibility = View.VISIBLE
            holder.txtWating.visibility = View.GONE
            holder.imgEdit.visibility = View.GONE
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




        holder.imgEdit.visibility = View.GONE
        holder.relativeProfileDetail.visibility = View.GONE
        holder.relativePrice.visibility = View.VISIBLE





        val inputDateTime = data[position].pickup_date.toString() + ", " + data[position].pickup_time.toString()

        holder.txtTime.text = convertDateTime(inputDateTime)
//        holder.txtTime.text = inputDateTime.toString()
        holder.txtStatusName.text = data[position].status
        holder.txtCar.text = data[position].vehicle_type.toString() + " , " + data[position].no_of_passemger + " Passengers, "+ data[position].luggage + " Luggage"
        holder.txtPickUpAddress.text = data[position].pickup_location
        holder.txtDropAddress.text = data[position].drop_location
        holder.txtAmount.text = "$" + data[position].price.toString()
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
//        var icPhone: ImageView = itemView.findViewById<ImageView>(R.id.icPhone)
        var imgEdit: ImageView = itemView.findViewById<ImageView>(R.id.imgEdit)
        var relativeProfileDetail: RelativeLayout = itemView.findViewById(R.id.relativeProfileDetail)
        var relativePrice: RelativeLayout = itemView.findViewById(R.id.relativePrice)


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