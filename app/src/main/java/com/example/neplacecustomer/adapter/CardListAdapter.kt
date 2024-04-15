package com.example.neplacecustomer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.CardlistBinding
import com.example.neplacecustomer.model.GetCardsData

class CardListAdapter(var context: Context, var cardlist: List<GetCardsData>, var onclick:ondeleteCrd):RecyclerView.Adapter<CardListAdapter.ViewHolder>(),
    View.OnClickListener {


    var selectedcardList:List<String>?=ArrayList()
    var selectedItemPos:Int?=null
    var lastItemSelectedPos:Int=-1

    inner class ViewHolder(var binding : CardlistBinding,):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                selectedItemPos = adapterPosition
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos as Int
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos as Int
                }
                notifyItemChanged(selectedItemPos!!)
            }
        }
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListAdapter.ViewHolder {

        var binding = CardlistBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CardListAdapter.ViewHolder, position: Int) {

        if(position == selectedItemPos){
           holder.binding.layoutCard.setBackgroundResource(R.drawable.white_border_ronded)
        } else{
            holder.binding.layoutCard.setBackgroundResource(R.drawable.edittext_corner_gray)
        }

        holder.binding.cardnumber.setText("************"+cardlist[position].card_number)
        holder.binding.imgCrossCard.setOnClickListener {
          onclick.ondelete(cardlist[position].id.toString())
      }
        holder.binding.layoutCard.setOnClickListener{
            onclick.onPlanClick(cardlist[position].id)
        }
    }

    override fun getItemCount(): Int {
    return cardlist.size
    }

    interface ondeleteCrd{
        fun ondelete(id:String)
        fun onPlanClick(card_id: Int)
    }

    override fun onClick(v: View?) {
        when(v!!.id){


        }
    }


}