package com.neplace.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.GetCards
import com.neplace.customer.model.GetGoogleKeyModel
import com.neplace.customer.model.UserNotificationModel
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