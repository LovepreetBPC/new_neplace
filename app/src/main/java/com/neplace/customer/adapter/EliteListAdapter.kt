package com.neplace.customer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neplace.customer.R
import com.neplace.customer.model.PlanDetailModel

class EliteListAdapter(var data: PlanDetailModel?) : RecyclerView.Adapter<EliteListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.elite_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtTitle.setText(data!!.data.description.get(position))


    }

    override fun getItemCount(): Int {
        return data!!.data.description.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)
    }


}