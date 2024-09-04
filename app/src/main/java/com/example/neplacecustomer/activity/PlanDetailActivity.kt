package com.example.neplacecustomer.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neplacecustomer.R
import com.example.neplacecustomer.adapter.PlanDecListItemAdapter
import com.example.neplacecustomer.databinding.ActivityPlanDetailBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.PlanDetailModel
import com.example.neplacecustomer.viewmodel.GetPlanDetailViewModel

class PlanDetailActivity : BaseActivity() {

    lateinit var binding: ActivityPlanDetailBinding

    lateinit var viewModel: GetPlanDetailViewModel

    var id = ""
    var title = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plan_detail)

        id = intent.getStringExtra("id").toString()
        title = intent.getStringExtra("title").toString()
        binding.txtTitle.text = title
        initViews()


        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.txtContinue.setOnClickListener {
            startActivity(
                Intent(this, PaymentTypeActivity::class.java).putExtra("id", id)
                    .putExtra("price", binding.txtPrice.text.toString())

            )
            finish()
        }

        binding.txtPrivacy.setOnClickListener{
            startActivity(Intent(this@PlanDetailActivity, PrivacyPolicyActivity::class.java))
            finish()
        }

    }

    private fun initViews() {

        viewModel = ViewModelProvider(this)[GetPlanDetailViewModel::class.java]
        viewModel.getPlan(id)


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

    private fun processEdit(data: PlanDetailModel?) {
//        ToastMsg(data!!.message.toString())
        binding.txtPrice.text = "$" + data!!.data.price + "/"
        binding.txtMonth.text = data.data.type
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PlanDecListItemAdapter(this, data.data.description)
        binding.recyclerView.adapter = adapter

    }

}