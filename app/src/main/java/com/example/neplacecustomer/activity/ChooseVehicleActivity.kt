package com.example.neplacecustomer.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neplacecustomer.R
import com.example.neplacecustomer.adapter.ChooseVehicleAdapter
import com.example.neplacecustomer.databinding.ActivityChooseVehicleBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.GetVehicleData
import com.example.neplacecustomer.model.GetVehicleModel
import com.example.neplacecustomer.viewmodel.GetVehicleViewModel

class ChooseVehicleActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding : ActivityChooseVehicleBinding

    lateinit var viewModel: GetVehicleViewModel

    var pickup_lat: String = ""
    var pickup_long: String=""
    var drop_lat: String= ""
    var drop_long: String=""
    var pickup_location: String=""
    var drop_location: String=""
    var pickup_city: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_vehicle)

        pickup_lat = intent.getStringExtra("pickup_lat").toString()
        pickup_long = intent.getStringExtra("pickup_long").toString()
        drop_lat = intent.getStringExtra("drop_lat").toString()
        drop_long = intent.getStringExtra("drop_long").toString()
        pickup_location = intent.getStringExtra("pickup_location").toString()
        drop_location = intent.getStringExtra("drop_location").toString()
        pickup_city = intent.getStringExtra("pickup_city").toString()

        setOnClick()
        initViews()


    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.imgEdit.setOnClickListener(this)


        binding.pickupLocation.setText(pickup_location)
        binding.dropLocation.setText(drop_location)

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack ->{
                finish()

            }
            R.id.imgEdit-> {
                startActivity(Intent(this,BookRideActivity::class.java))
                finish()
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
        ToastMsg(msg.toString())
    }

    private fun processEdit(data: GetVehicleModel?) {
//        ToastMsg(data!!.message.toString())
        Log.e("choose_vehicle", "processEdit: "+data!!.message.toString())

//            binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
            binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
            val adapter = ChooseVehicleAdapter(this,data.data , object : ChooseVehicleAdapter.itemClick{
                override fun itemClick(position: Int, getVehicleData: GetVehicleData) {

                    val intent = Intent(this@ChooseVehicleActivity,ScheduleRideActivity::class.java)
                    intent.putExtra("vehicleType",getVehicleData.name)
                    intent.putExtra("seat",getVehicleData.seat)
                    intent.putExtra("pickup_location",pickup_location)
                    intent.putExtra("drop_location",drop_location)
                    intent.putExtra("pickup_lat",pickup_lat)
                    intent.putExtra("pickup_long",pickup_long)
                    intent.putExtra("drop_lat",drop_lat)
                    intent.putExtra("drop_long",drop_long)
                    intent.putExtra("pickup_city",pickup_city)
                    startActivity(intent)
                }
            })
            binding.recyclerView.adapter = adapter

    }


}