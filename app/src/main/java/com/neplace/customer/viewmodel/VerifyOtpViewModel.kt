package com.neplace.customer.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.neplace.customer.model.VerifyOtpModel
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import kotlinx.coroutines.launch


class VerifyOtpViewModel : ViewModel() {
    var VerifyOtpResponse: MutableLiveData<BaseResponse<VerifyOtpModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun verifyOtpUser(number: String, otp:String ,device_token: String) {
        VerifyOtpResponse.value=null
        VerifyOtpResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.verifyOtp(number,otp, device_token)

                if (response.code() == 200) {
                    VerifyOtpResponse.value=null

                    VerifyOtpResponse.value = BaseResponse.Success(response.body())
                } else {
                    VerifyOtpResponse.value=null

                    VerifyOtpResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
                Log.e("lovepreet098767890", "verifyOtpUser: "+ae.message.toString() )
            }

        }

    }

}