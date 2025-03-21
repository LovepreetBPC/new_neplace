package com.neplace.customer.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neplace.customer.R
import com.neplace.customer.adapter.NotificationAdapter
import com.neplace.customer.databinding.ActivityNotificationBinding
import com.neplace.customer.login.BaseActivity
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.model.UserNotificationModel
import com.neplace.customer.viewmodel.DeleteNotificationViewModel
import com.neplace.customer.viewmodel.DeleteSingleNotificationViewModel
import com.neplace.customer.viewmodel.GetNotificationViewModel
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
                    //ToastMsg(it.data!!.message)
                }

                is BaseResponse.Error -> {
                    dismissProgress()
//                    ToastMsg(it.msg.toString())
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
                    ToastMsg(it.data!!.message)

                }

                is BaseResponse.Error -> {
                    dismissProgress()
//                    ToastMsg(it.msg.toString())
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
                    ToastMsg(it.data!!.message)
//                    ToastMsg(it.data!!.message.toString())

                }

                is BaseResponse.Error -> {
                    dismissProgress()
//                    ToastMsg(it.msg.toString())
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
                //ToastMsg("No Data Found")
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