package com.example.neplacecustomer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityRatingDriverBinding

class RatingDriverActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityRatingDriverBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating_driver)
        setOnClick()
    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.relativeSubmit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.relativeSubmit -> {
                startActivity(Intent(this, DriveDetailsActivity::class.java))
            }
        }
    }
}