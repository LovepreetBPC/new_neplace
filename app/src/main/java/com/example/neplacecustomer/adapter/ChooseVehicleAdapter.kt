package com.example.neplacecustomer.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neplacecustomer.R
import com.example.neplacecustomer.model.GetVehicleData
import com.example.neplacecustomer.common.Constant

class ChooseVehicleAdapter(val context: Context,val data: List<GetVehicleData> , val handler :itemClick) :
    RecyclerView.Adapter<ChooseVehicleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_vehicle_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(Constant.BASEURL + data[position].image)
            .error(R.mipmap.car_img)
            .centerInside()
            .into(holder.imgCar)

        holder.itemView.setOnClickListener{
            handler.itemClick(position,data[position])
        }

        holder.txtPrice.text = if (data[position].base_price != null) "$"+data[position].base_price.toString() else "N/A"
        holder.txtCarName.text = if (data[position].name != null) data[position].name.toString() else "N/A"
        holder.txtSheet.text = if (data[position].seat != null) data[position].seat.toString() else "N/A"

    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var imgCar = itemView.findViewById<ImageView>(R.id.imgCar)
        var txtCarName = itemView.findViewById<TextView>(R.id.txtCarName)
        var txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        var txtSheet = itemView.findViewById<TextView>(R.id.txtSheet)

    }

    interface itemClick{
        fun itemClick(position: Int, getVehicleData: GetVehicleData)
    }
}