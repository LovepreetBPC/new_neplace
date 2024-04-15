package com.example.neplacecustomer.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neplacecustomer.R
import com.example.neplacecustomer.adapter.NotificationAdapter
import com.example.neplacecustomer.databinding.ActivityNotificationBinding
import com.example.neplacecustomer.login.BaseActivity
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.model.UserNotificationModel
import com.example.neplacecustomer.viewmodel.DeleteNotificationViewModel
import com.example.neplacecustomer.viewmodel.DeleteSingleNotificationViewModel
import com.example.neplacecustomer.viewmodel.GetNotificationViewModel
import okhttp3.internal.notify

class NotificationActivity : BaseActivity() {
    lateinit var binding: ActivityNotificationBinding

    lateinit var deleteNotificationViewModel: DeleteNotificationViewModel
    lateinit var deleteSingleNotificationViewModel: DeleteSingleNotificationViewModel
    lateinit var getNotificationViewModel: GetNotificationViewModel
    lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.txtClearAll.setOnClickListener {

            deleteNotificationViewModel.deleteNotification()
        }
        initViews()
    }


    private fun initViews() {
        getNotificationViewModel = ViewModelProvider(this)[GetNotificationViewModel::class.java]

        deleteNotificationViewModel =
            ViewModelProvider(this)[DeleteNotificationViewModel::class.java]
        deleteSingleNotificationViewModel =
            ViewModelProvider(this)[DeleteSingleNotificationViewModel::class.java]


        getNotificationViewModel.getNotification()




        getNotificationViewModel.getNotificationResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    setRecyclerView(it.data)
                }

                is BaseResponse.Error -> {
                    dismissProgress()
                    ToastMsg(it.msg.toString())
                }

                else -> {
                    dismissProgress()
                }
            }
        }

        deleteSingleNotificationViewModel.deleteSingleNotificationResponse.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    getNotificationViewModel.getNotification()
//                    ToastMsg(it.data!!.message.toString())

                }

                is BaseResponse.Error -> {
                    dismissProgress()
                    ToastMsg(it.msg.toString())
                }

                else -> {
                    dismissProgress()
                }
            }
        }

        deleteNotificationViewModel.deleteNotification.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showProgress()
                }

                is BaseResponse.Success -> {
                    dismissProgress()
                    getNotificationViewModel.getNotification()
//                    ToastMsg(it.data!!.message.toString())

                }

                is BaseResponse.Error -> {
                    dismissProgress()
                    ToastMsg(it.msg.toString())
                }

                else -> {
                    dismissProgress()
                }
            }
        }
    }


    private fun setRecyclerView(data: UserNotificationModel?) {


        if (data != null) {
            if (data.data.size < 1) {
                ToastMsg("No Data Found")
            } else {
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                adapter = NotificationAdapter(data!!.data,
                    object : NotificationAdapter.DeleteNotificationHandler {
                        override fun onClick(id: String) {
                            deleteSingleNotificationViewModel.deleteSingleNotification(id)
                        }

                    })
                binding.recyclerView.adapter = adapter
            }
        }
    }
}