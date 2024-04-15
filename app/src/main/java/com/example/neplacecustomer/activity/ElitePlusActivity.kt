package com.example.neplacecustomer.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neplacecustomer.R
import com.example.neplacecustomer.adapter.EliteListAdapter
import com.example.neplacecustomer.databinding.ActivityElitePlusBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.PlanDetailModel
import com.example.neplacecustomer.viewmodel.GetPlanDetailViewModel

class ElitePlusActivity : BaseActivity(),View.OnClickListener {
    lateinit var binding: ActivityElitePlusBinding


    lateinit var viewModel: GetPlanDetailViewModel

    var id = "1"
    var title = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_elite_plus)
        setOnClick()
    }

    private fun setOnClick() {
        binding.imgBack.setOnClickListener(this)

        initViews()
    }


    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imgBack -> {
                finish()
            }
        }
    }

    private fun  initViews(){

        viewModel= ViewModelProvider(this)[GetPlanDetailViewModel::class.java]
        viewModel.getPlan(id)


        viewModel.planResponse.observe(this){
            when(it){
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    binding.txtAmount.text == "$"+it.data!!.data.price + "/"+ it.data.data.type
                    setRecyclerView(it.data)
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


    private fun setRecyclerView(data: PlanDetailModel?) {

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = EliteListAdapter(data)
        binding.recyclerView.adapter = adapter
    }
}