package com.example.neplacecustomer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.neplacecustomer.R
import com.example.neplacecustomer.activity.SubscriptionPlansActivity
import com.example.neplacecustomer.model.PlanData

class SubscriptionPlanAdapter(
    val context: SubscriptionPlansActivity,
    val data: List<PlanData>,
    val handler: itemClick
) : RecyclerView.Adapter<SubscriptionPlanAdapter.ViewHolder>() {

    var mSelectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            mSelectedPosition = position
            notifyDataSetChanged()
            handler.itemClick(position, data[position])

        }
        holder.txtMore.setOnClickListener {
            mSelectedPosition = position
            notifyDataSetChanged()
            handler.moreClick(position, data[position])

        }


        if (mSelectedPosition == position) {
            holder.imgSelected.setImageResource(R.mipmap.ic_selected)


        } else {

            holder.imgSelected.setImageResource(R.mipmap.ic_unselected)

        }


        holder.imgSelected.setOnClickListener {

        }
        holder.txtPrice.text = "$" + data[position].price.toString()
        holder.txtPlanName.text = data[position].name.toString()
        holder.txtDescription.text = data[position].description.toString()
        holder.txtPlanMonthly.text = data[position].tagline.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var imgSelected = itemView.findViewById<ImageView>(R.id.imgSelected)
        var txtPlanName = itemView.findViewById<TextView>(R.id.txtPlanName)
        var txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)
        var txtDescription = itemView.findViewById<TextView>(R.id.txtDescription)
        var txtPlanMonthly = itemView.findViewById<TextView>(R.id.txtPlanMonthly)
        var txtMore = itemView.findViewById<TextView>(R.id.txtMore)

    }

    interface itemClick {
        fun itemClick(position: Int, plaData: PlanData)
        fun moreClick(position: Int, plaData: PlanData)
    }


}