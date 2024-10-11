package com.neplace.customer.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.neplace.customer.R
import com.neplace.customer.databinding.BillingInfoListItemBinding
import com.neplace.customer.model.RideHistoryData
import java.text.SimpleDateFormat
import java.util.Locale

class BillingInfoAdapter(var context: Context, val data: List<RideHistoryData>):RecyclerView.Adapter<BillingInfoAdapter.ViewHolder>() {

    inner class ViewHolder(var binding : BillingInfoListItemBinding,):RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingInfoAdapter.ViewHolder {
        val binding = BillingInfoListItemBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: BillingInfoAdapter.ViewHolder, position: Int) {

        if (data[position].ridestatus == "Active") {
            holder.binding.txtStatusName.setTextColor(context.getColor(R.color.green))
        }else if (data[position].ridestatus =="active"){
            holder.binding.txtStatusName.setTextColor(context.getColor(R.color.green))
        }else{
            holder.binding.txtStatusName.setTextColor(context.getColor(R.color.yellow))

        }


        holder.binding.txtstatus.text = data[position].ridestatus.toString()
        holder.binding.txtPickUpAddress.text = data[position].pickup_location
        holder.binding.txtDropAddress.text = data[position].drop_location
        holder.binding.txtprice.text = "$"+data[position].price
        holder.binding.txtmiles.text = data[position].miles + "Miles"
        holder.binding.txtTime.text = "Trip id-#${data[position].trip_id}"

        val inputDateTime = data[position].pickup_date.toString() + ", " + data[position].pickup_time.toString()

        holder.binding.txtDate.text = convertDateTime(inputDateTime)

    }

    override fun getItemCount(): Int {
       return data.size
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