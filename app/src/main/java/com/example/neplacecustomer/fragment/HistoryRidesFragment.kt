package com.example.neplacecustomer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neplacecustomer.R
import com.example.neplacecustomer.activity.AllRidesActivity
import com.example.neplacecustomer.adapter.TripHistoryAdapter
import com.example.neplacecustomer.databinding.FragmentHistoryRidesBinding
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.RideHistoryData
import com.example.neplacecustomer.model.RideHistoryModel
import com.example.neplacecustomer.viewmodel.RideHistoryViewModel
import com.example.neplacecustomer.viewmodel.RideUpcomingViewModel

class HistoryRidesFragment() : Fragment() ,AllRidesActivity.Refreshable{

    lateinit var binding: FragmentHistoryRidesBinding

    lateinit var viewModel: RideUpcomingViewModel
    lateinit var rideHistoryViewModel: RideHistoryViewModel
    var type = "completed"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_rides, container, false)
//        getridehistory()
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

    private fun getridehistory(){
        rideHistoryViewModel = ViewModelProvider(this)[RideHistoryViewModel::class.java]
        rideHistoryViewModel.getRideHistory()
        rideHistoryViewModel.rideHistoryResponse.observe(viewLifecycleOwner){

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
            val adapter = TripHistoryAdapter(requireContext(),data ,type)
            binding.recyclerView.adapter = adapter
        }
    }


    override fun refreshDataAsync() {
        initViews()
    }
}