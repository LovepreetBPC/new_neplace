package com.neplace.customer.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neplace.customer.databinding.ActivityCreatePlanBinding

class CreatePlanActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreatePlanBinding

    var pickup_location=""
    var drop_location=""
    var pickup_lat=""
    var pickup_lon=""
    var drop_lat=""
    var drop_long=""
    var pickup_city=""
    var vehicleType=""
    var seat=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        vehicleType = intent.getStringExtra("vehicleType").toString()
        seat = intent.getStringExtra("seat").toString()
        pickup_location = intent.getStringExtra("pickup_location").toString()
        drop_location = intent.getStringExtra("drop_location").toString()
        pickup_lat = intent.getStringExtra("pickup_lat").toString()
        pickup_lon = intent.getStringExtra("pickup_lon").toString()
        drop_lat = intent.getStringExtra("drop_lat").toString()
        drop_long = intent.getStringExtra("drop_long").toString()
        pickup_city = intent.getStringExtra("pickup_city").toString()




        binding.relativeContinue.setOnClickListener {
            val intent = Intent(this, ScheduleRideActivity::class.java)
            intent.putExtra("vehicleType", vehicleType)
            intent.putExtra("seat", seat)
            intent.putExtra("pickup_location", pickup_location)
            intent.putExtra("drop_location", drop_location)
            intent.putExtra("pickup_lat", pickup_lat)
            intent.putExtra("pickup_long", pickup_lon)
            intent.putExtra("drop_lat", drop_lat)
            intent.putExtra("drop_long", drop_long)
            intent.putExtra("pickup_city", pickup_city)
            intent.putExtra("alacart", "1")
            startActivity(intent)
        }
        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.relativeSeePlan.setOnClickListener {
            startActivity(Intent(this, SubscriptionPlansActivity::class.java))
        }
    }


}