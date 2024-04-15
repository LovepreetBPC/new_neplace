package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.UserNotificationModel
import kotlinx.coroutines.launch


class DeleteSingleNotificationViewModel : ViewModel() {
    var deleteSingleNotificationResponse: MutableLiveData<BaseResponse<UserNotificationModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun deleteSingleNotification(id:String) {
        deleteSingleNotificationResponse.value=null
        deleteSingleNotificationResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.deleteSingleNotification(id)
                if (response.code() == 200) {
                    deleteSingleNotificationResponse.value=null
                    deleteSingleNotificationResponse.value = BaseResponse.Success(response.body())
                } else {
                    deleteSingleNotificationResponse.value=null
                    deleteSingleNotificationResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}