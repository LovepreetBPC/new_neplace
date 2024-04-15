package com.example.neplacecustomer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neplacecustomer.R
import com.example.neplacecustomer.model.RideHistoryData

class TripUpcomingAdapter(val context: Context, val data: List<RideHistoryData>) :RecyclerView.Adapter<TripUpcomingAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_ride_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        holder.itemView.setOnClickListener{
//            context.startActivity(Intent(context,ChooseVehicleActivity::class.java))
//        }
    }

    override fun getItemCount(): Int {
        return 6
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

    }


}