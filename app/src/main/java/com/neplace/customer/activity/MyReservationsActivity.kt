package com.neplace.customer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.neplace.customer.R
import com.neplace.customer.databinding.ActivityMyReservationsBinding
import com.neplace.customer.demo.ViewPagerAdapter
import com.neplace.customer.fragment.HistoryRidesFragment

class MyReservationsActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyReservationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_reservations)


        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HistoryRidesFragment(), "Upcoming".lowercase())
        adapter.addFragment(HistoryRidesFragment(), "Completed".lowercase())
        adapter.addFragment(HistoryRidesFragment(), "Cancelled".lowercase())
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
//        binding.tabs.addOnTabSelectedListener(this)

    }
    }

