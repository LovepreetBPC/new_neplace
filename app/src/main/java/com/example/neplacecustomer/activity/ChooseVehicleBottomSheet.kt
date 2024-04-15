package com.example.neplacecustomer.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.neplacecustomer.R
import com.example.neplacecustomer.adapter.ChooseVehicleAdapter
import com.example.neplacecustomer.databinding.ChooseVehicleBottomSheetBinding
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.GetVehicleData
import com.example.neplacecustomer.model.GetVehicleModel
import com.example.neplacecustomer.viewmodel.GetVehicleViewModel

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseVehicleBottomSheet(
    var pickup_location: String,
    var drop_location: String,
    var pickup_lat: String,
    var pickup_long: String,
    var drop_lat: String,
    var drop_long: String,
    var pickup_city: String,
    val subscribePlan: Boolean
) : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var pDialog: ProgressDialog

    lateinit var binding: ChooseVehicleBottomSheetBinding
    lateinit var viewModel: GetVehicleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ChooseVehicleBottomSheetBinding.inflate(layoutInflater)

        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme) // Add this line
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireView().parent as View).setBackgroundColor(Color.TRANSPARENT)

        setOnClick()
        initViews()

        pDialog = ProgressDialog(requireContext())
        pDialog.setCancelable(false)
        pDialog.setMessage("Loading...")

    }

    private fun setOnClick() {
        binding.imgEdit.setOnClickListener(this)


        binding.pickupLocation.setText(pickup_location)
        binding.dropLocation.setText(drop_location)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.imgEdit -> {
                startActivity(Intent(requireContext(), BookRideActivity::class.java))
            }


        }
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[GetVehicleViewModel::class.java]
        viewModel.getVehicleUser("1")

        viewModel.getVehicleResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    processEdit(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }

                else -> {
                    dismissProgress()
                }
            }
        }


    }

    private fun processError(msg: String?) {
        Toast.makeText(requireContext(), "" + msg.toString(), Toast.LENGTH_SHORT).show()

    }

    private fun processEdit(data: GetVehicleModel?) {
//        ToastMsg(data!!.message.toString())
        Log.e("choose_vehicle", "processEdit: " + data!!.message.toString())

        if (data.data.size < 3) {
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        }

//            binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val adapter = ChooseVehicleAdapter(requireContext(), data.data, object : ChooseVehicleAdapter.itemClick {
                override fun itemClick(position: Int, getVehicleData: GetVehicleData) {


                    if (subscribePlan) {
                        val intent = Intent(requireContext(), ScheduleRideActivity::class.java)
                        intent.putExtra("vehicleType", getVehicleData.name)
                        intent.putExtra("seat", getVehicleData.seat)
                        intent.putExtra("pickup_location", pickup_location)
                        intent.putExtra("drop_location", drop_location)
                        intent.putExtra("pickup_lat", pickup_lat)
                        intent.putExtra("pickup_long", pickup_long)
                        intent.putExtra("drop_lat", drop_lat)
                        intent.putExtra("drop_long", drop_long)
                        intent.putExtra("pickup_city", pickup_city)
                        startActivity(intent)

                    } else {
                        startActivity(Intent(context, CreatePlanActivity::class.java)
                            .putExtra("vehicleType",getVehicleData.name)
                            .putExtra("seat",getVehicleData.seat)
                            .putExtra("pickup_location",pickup_location)
                            .putExtra("drop_location",  drop_location)
                            .putExtra("pickup_lat",  pickup_lat.toString())
                            .putExtra("pickup_lon",  pickup_long.toString())
                            .putExtra("drop_lat",  drop_lat.toString())
                            .putExtra("drop_long",  drop_long.toString())
                            .putExtra("pickup_city",  pickup_city.toString())
                        )
                    }





                }
            })
        binding.recyclerView.adapter = adapter

    }


    fun showProgress() {
        pDialog.show()
    }

    fun dismissProgress() {
        pDialog.dismiss()
    }

}


