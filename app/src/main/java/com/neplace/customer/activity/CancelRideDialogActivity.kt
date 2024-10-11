package com.neplace.customer.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.neplace.customer.databinding.ActivityCancelRideDialogBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.viewmodel.GetRideCancelChargesViewModel

class CancelRideDialogActivity : BaseActivity() {


    lateinit var binding: ActivityCancelRideDialogBinding

    var ride_id = ""
    var ride_amount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelRideDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ride_id = intent.getStringExtra("ride_id").toString()
        ride_amount = intent.getStringExtra("ride_amount").toString()
        Log.e("TAG_CancelActivity", "onCreate: $ride_id   ,  ride_amount  -> $ride_amount")
        binding.txtTitle.text = "in case of cancellation \$$ride_amount will be deducted from your A/c automatically"



        binding.relativeNoContinue.setOnClickListener {
            finish()
        }

        binding.relativeYesCancel.setOnClickListener {
            startActivity(Intent(this, PaymentTypeActivity::class.java).putExtra("ride_id", ride_id).putExtra("type", "cancel_ride"))
            finish()

        }

    }


}