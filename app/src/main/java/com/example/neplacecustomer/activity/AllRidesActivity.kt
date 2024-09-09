package com.example.neplacecustomer.activity

import android.content.Intent
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
    var type = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_rides)

        type = intent.getStringExtra("type").toString()

        binding.imgBack.setOnClickListener{
            if (type == "Book"){
                startActivity(Intent(this,SideMenuActivity::class.java))
                finish()
            }else{
                finish()
            }
        }
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(UpcomingRideFragment(), "Upcoming".lowercase())
        adapter.addFragment(HistoryRidesFragment(), "Historical".lowercase())
        adapter.addFragment(CanceledRideFragment(), "Canceled".lowercase())
        binding.viewPager.adapter = adapter

        // bind the viewPager with the TabLayout.
        binding.tabs.setupWithViewPager(binding.viewPager)


        binding.tabs.addOnTabSelectedListener(this)

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val fragment = (binding.viewPager.adapter as ViewPagerAdapter).getItem(binding.tabs.selectedTabPosition)
        if (fragment is Refreshable && fragment.isAdded && !fragment.isDetached) {
            fragment.refreshDataAsync()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}
    override fun onTabReselected(tab: TabLayout.Tab?) {}

    interface Refreshable {
        fun refreshDataAsync()
    }
}
