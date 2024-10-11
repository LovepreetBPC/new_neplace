package com.neplace.customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neplace.customer.R
import com.neplace.customer.activity.AllRidesActivity
import com.neplace.customer.adapter.CancelledRidesAdapter
import com.neplace.customer.databinding.FragmentUpcomingRideBinding
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.RideHistoryData
import com.neplace.customer.model.RideHistoryModel
import com.neplace.customer.viewmodel.RideUpcomingViewModel

class CanceledRideFragment() : Fragment(),AllRidesActivity.Refreshable {


    lateinit var binding: FragmentUpcomingRideBinding

    lateinit var viewModel: RideUpcomingViewModel

    var type = "canceled"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming_ride, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }
    private fun initViews() {
        viewModel = ViewModelProvider(this)[RideUpcomingViewModel::class.java]
        viewModel.getRide(type)

        viewModel.rideUpcomingResponse.observe(viewLifecycleOwner) {
            when (it) {
                is BaseResponse.Loading -> {
//                    showProgress()
                }

                is BaseResponse.Success -> {
//                    dismissProgress()
                    processEdit(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
//                    dismissProgress()
                }
            }
        }
    }

    private fun processEdit(data: RideHistoryModel?) {
//        allRidesActivity.ToastMsg(data!!.message.toString())
        setRecyclerView(data!!.data)
    }

    private fun processError(msg: String?) {
//        ToastMsg(msg.toString())
    }


    private fun setRecyclerView(data: List<RideHistoryData>) {

        if (data.size<1){
//            Toast.makeText(context, "No Data Found!!", Toast.LENGTH_SHORT).show()
            binding.txtNoData.visibility = View.VISIBLE
        }else{
            binding.txtNoData.visibility = View.GONE
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = CancelledRidesAdapter(requireContext(), data, type)
            binding.recyclerView.adapter = adapter
        }

    }



    override fun refreshDataAsync() {
        initViews()
    }
}