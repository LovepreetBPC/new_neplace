package com.neplace.customer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityDriverAcceptanceBinding

class DriverAcceptanceActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var binding: ActivityDriverAcceptanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_acceptance)
        setOnClick()
    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.txtConfirm.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                finish()
            }
            R.id.txtConfirm -> {
                startActivity(Intent(this, RatingDriverActivity::class.java))
            }
        }

    }
}
