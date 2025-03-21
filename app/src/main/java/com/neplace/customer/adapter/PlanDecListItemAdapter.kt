package com.neplace.customer.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neplace.customer.R

class PlanDecListItemAdapter(val context: Context, val data: List<String>) :RecyclerView.Adapter<PlanDecListItemAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_dec_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        Log.d("dataPos", "onBindViewHolder: ${data[position]}")
        holder.txtDec.text= data[position].toString()

//        holder.itemView.setOnClickListener{
//            context.startActivity(Intent(context,ChooseVehicleActivity::class.java))
//        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var txtDec = itemView.findViewById<TextView>(R.id.txtDec)
    }


}