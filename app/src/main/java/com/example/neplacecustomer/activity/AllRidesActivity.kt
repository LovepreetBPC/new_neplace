package com.example.neplacecustomer.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.neplacecustomer.R
import com.example.neplacecustomer.adapter.ViewPagerAdapter
import com.example.neplacecustomer.databinding.ActivityAllRidesBinding
import com.example.neplacecustomer.fragment.CanceledRideFragment
import com.example.neplacecustomer.fragment.HistoryRidesFragment
import com.example.neplacecustomer.fragment.UpcomingRideFragment
import com.example.neplacecustomer.login.BaseActivity
import com.google.android.material.tabs.TabLayout

class AllRidesActivity : BaseActivity(), TabLayout.OnTabSelectedListener {

    lateinit var binding: ActivityAllRidesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_rides)

        binding.imgBack.setOnClickListener{
            finish()
        }
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(UpcomingRideFragment(), "Upcoming")
        adapter.addFragment(HistoryRidesFragment(), "Historical")
        adapter.addFragment(CanceledRideFragment(), "Canceled")
        // Adding the Adapter to the ViewPager
        binding.viewPager.adapter = adapter

        // bind the viewPager with the TabLayout.
        binding.tabs.setupWithViewPager(binding.viewPager)


        binding.tabs.addOnTabSelectedListener(this)

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val fragment = (binding.viewPager.adapter as ViewPagerAdapter).getItem(binding.tabs.selectedTabPosition)
        if (fragment is Refreshable && fragment.isAdded && !fragment.isDetached) {
            fragment.refreshData()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}
    override fun onTabReselected(tab: TabLayout.Tab?) {}

    interface Refreshable {
        fun refreshData()
    }

}
