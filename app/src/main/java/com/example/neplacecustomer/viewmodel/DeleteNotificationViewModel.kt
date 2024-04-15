package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.UserNotificationModel
import kotlinx.coroutines.launch


class DeleteNotificationViewModel : ViewModel() {
    var deleteNotification: MutableLiveData<BaseResponse<UserNotificationModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun deleteNotification() {
        deleteNotification.value=null
        deleteNotification.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.deleteNotification()
                if (response.code() == 200) {
                    deleteNotification.value=null
                    deleteNotification.value = BaseResponse.Success(response.body())
                } else {
                    deleteNotification.value=null
                    deleteNotification.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}