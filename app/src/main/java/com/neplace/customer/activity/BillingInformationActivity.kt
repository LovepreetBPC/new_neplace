package com.neplace.customer.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neplace.customer.adapter.BillingInfoAdapter
import com.neplace.customer.databinding.ActivityBillingInformationBinding
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.RideHistoryModel
import com.neplace.customer.viewmodel.RideUpcomingViewModel

class BillingInformationActivity : AppCompatActivity() {

    lateinit var binding: ActivityBillingInformationBinding
    lateinit var billingInfoAdapter: BillingInfoAdapter
    lateinit var viewModel: RideUpcomingViewModel
    var type = "history"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillingInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[RideUpcomingViewModel::class.java]
        viewModel.getRide(type)

        viewModel.rideUpcomingResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
//                    showProgress()
                }

                is BaseResponse.Success -> {
//                    dismissProgress()
                    if (it.data!!.status) {
                        processEdit(it.data)
                    }
                }

                is BaseResponse.Error -> {
                    Toast.makeText(this, "" + it.msg, Toast.LENGTH_SHORT).show()
                }

                else -> {
//                    dismissProgress()
                }
            }
        }

    }

    private fun processEdit(data: RideHistoryModel) {

        if (data.data.isEmpty()){
//            Toast.makeText(context, "No Data Found!!", Toast.LENGTH_SHORT).show()
            binding.txtNoData.visibility = View.VISIBLE
        }else{

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            billingInfoAdapter = BillingInfoAdapter(this,data.data)
            binding.recyclerView.adapter = billingInfoAdapter
        }

    }
}