package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.UserNotificationModel
import kotlinx.coroutines.launch


class GetNotificationViewModel : ViewModel() {
    var getNotificationResponse: MutableLiveData<BaseResponse<UserNotificationModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getNotification() {
        getNotificationResponse.value=null
        getNotificationResponse.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = userRepo.getNotification()
                if (response.code() == 200) {
                    getNotificationResponse.value=null
                    getNotificationResponse.value = BaseResponse.Success(response.body())
                } else {
                    getNotificationResponse.value=null
                    getNotificationResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}