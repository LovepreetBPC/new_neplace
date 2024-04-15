package com.example.neplacecustomer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityThanksBinding

class ThanksActivity : AppCompatActivity() {

    lateinit var binding: ActivityThanksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thanks)

        binding.relativeOk.setOnClickListener {
            startActivity(Intent(this,BookRideActivity::class.java))
            finish()
        }

    }
}