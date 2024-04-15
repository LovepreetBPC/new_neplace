package com.example.neplacecustomer.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.neplacecustomer.R
import com.example.neplacecustomer.databinding.ActivityWaitingForDriverBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.viewmodel.CancelRideViewModel
import com.example.neplacecustomer.viewmodel.GetPlanDetailViewModel

class WaitingForDriverActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityWaitingForDriverBinding

    lateinit var viewModel: CancelRideViewModel

    var user_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_waiting_for_driver)
        setOnClick()
        initViews()

    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.relativeContinue.setOnClickListener(this)
        binding.relativeChangePickTime.setOnClickListener(this)
        binding.relativeCancelRequest.setOnClickListener(this)
        user_id = intent.getStringExtra("user_id").toString()


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.relativeContinue -> {

                startActivity(Intent(this, DriverAcceptanceActivity::class.java))
            }
            R.id.relativeCancelRequest -> {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.booking_cancel_dialog_layout)
        val noContinue = dialog.findViewById(R.id.relativeNoContinue) as RelativeLayout
        val yesCancel = dialog.findViewById(R.id.relativeYesCancel) as RelativeLayout

        noContinue.setOnClickListener {
            viewModel.cancelRide(user_id)
            dialog.dismiss()
        }
        yesCancel.setOnClickListener {
            finish()
            dialog.dismiss()

        }

        dialog.show()

    }



    private fun  initViews(){

        viewModel= ViewModelProvider(this)[CancelRideViewModel::class.java]


        viewModel.cancelRideResponse.observe(this){
            when(it){
                is BaseResponse.Loading -> {
                    showProgress()
                }
                is BaseResponse.Success -> {
                    dismissProgress()
                    ToastMsg(it.data!!.message.toString())

                }
                is BaseResponse.Error -> {
                    processError(it.msg)
                    ToastMsg(it.msg.toString())
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

}