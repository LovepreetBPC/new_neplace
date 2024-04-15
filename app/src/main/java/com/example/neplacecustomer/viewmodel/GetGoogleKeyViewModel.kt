package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.GetCards
import com.example.neplacecustomer.model.GetGoogleKeyModel
import com.example.neplacecustomer.model.UserNotificationModel
import kotlinx.coroutines.launch

class GetGoogleKeyViewModel : ViewModel() {
    var getGoogleKeyResponse: MutableLiveData<BaseResponse<GetGoogleKeyModel>?> = MutableLiveData()
    private val getGoogleKeyRepo = UserRepository()

    fun getGoogleKey() {
        getGoogleKeyResponse.value=null
        getGoogleKeyResponse.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = getGoogleKeyRepo.getGoogleKey()
                if (response.code() == 200) {
                    getGoogleKeyResponse.value=null
                    getGoogleKeyResponse.value = BaseResponse.Success(response.body())
                } else {
                    getGoogleKeyResponse.value=null
                    getGoogleKeyResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}