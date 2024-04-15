package com.example.neplacecustomer.activity

import android.content.Intent
import android.os.Bundle
import com.example.neplacecustomer.databinding.ActivityCancelRideDialogBinding
import com.example.neplacecustomer.login.BaseActivity

class CancelRideDialogActivity : BaseActivity() {


    lateinit var binding: ActivityCancelRideDialogBinding


    var ride_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelRideDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ride_id = intent.getStringExtra("ride_id").toString()

        binding.relativeNoContinue.setOnClickListener {
            finish()
        }

        binding.relativeYesCancel.setOnClickListener {
            startActivity(Intent(this, PaymentTypeActivity::class.java).putExtra("ride_id", ride_id).putExtra("type", "cancel_ride"))
            finish()

        }

    }


}