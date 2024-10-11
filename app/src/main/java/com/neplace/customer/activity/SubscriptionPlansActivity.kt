package com.neplace.customer.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neplace.customer.R
import com.neplace.customer.adapter.SubscriptionPlanAdapter
import com.neplace.customer.databinding.ActivitySubscriptionPlansBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.GetPlanModel
import com.neplace.customer.model.PlanData
import com.neplace.customer.viewmodel.GetPlanViewModel

class SubscriptionPlansActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivitySubscriptionPlansBinding
    lateinit var viewModel: GetPlanViewModel
     var plan_id= "1"
     var plan_name= "Elite"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_plans)
        initViews()
        setOnClick()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[GetPlanViewModel::class.java]
        viewModel.getPlan()

        viewModel.planResponse.observe(this) {
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

    private fun processEdit(data: GetPlanModel?) {
//        ToastMsg(data!!.message.toString())


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter =
            SubscriptionPlanAdapter(this, data!!.data, object : SubscriptionPlanAdapter.itemClick {
                override fun itemClick(position: Int, plaData: PlanData) {
                    Log.e("selectedPlan", "itemClick: $position")
                    plan_id = plaData.id.toString()
                    plan_name = plaData.name.toString()

                }

                override fun moreClick(position: Int, plaData: PlanData) {
                    startActivity(Intent(this@SubscriptionPlansActivity, PlanDetailActivity::class.java)
                            .putExtra("id", plaData.id.toString()).putExtra("title", plaData.name)
                    )
                }
            })
        binding.recyclerView.adapter = adapter

    }


    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)
        binding.txtContinue.setOnClickListener(this)
        binding.txtSkip.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgBack -> {
                finish()
            }

            R.id.txtSkip -> {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }

            R.id.txtContinue -> {
                startActivity(Intent(this@SubscriptionPlansActivity, PlanDetailActivity::class.java)
                    .putExtra("id", plan_id.toString()).putExtra("title", plan_name)
                )

//                showDialog()

            }

        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.payment_success_dialog_layout)
        val noContinue = dialog.findViewById(R.id.relativeBookNow) as RelativeLayout
        val yesCancel = dialog.findViewById(R.id.relativeCancel) as RelativeLayout

        noContinue.setOnClickListener {
            startActivity(Intent(this, BookRideActivity::class.java))
            dialog.dismiss()
        }
        yesCancel.setOnClickListener {
            finish()
            dialog.dismiss()

        }

        dialog.show()

    }

}