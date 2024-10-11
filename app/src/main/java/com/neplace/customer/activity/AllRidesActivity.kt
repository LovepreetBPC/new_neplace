package com.neplace.customer.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.neplace.customer.R
import com.neplace.customer.adapter.ViewPagerAdapter
import com.neplace.customer.databinding.ActivityAllRidesBinding
import com.neplace.customer.fragment.CanceledRideFragment
import com.neplace.customer.fragment.HistoryRidesFragment
import com.neplace.customer.fragment.UpcomingRideFragment
import com.neplace.customer.login.BaseActivity
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

        refreshLayout()
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

    fun refreshLayout(){
        binding.swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(Runnable { // Stop animation (This will be after 3 seconds)

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


                binding.swipeContainer.setRefreshing(false)
            }, 2000) // Delay in millis
        })
    }


    override fun onResume() {
        super.onResume()
        refreshLayout()
    }
}
