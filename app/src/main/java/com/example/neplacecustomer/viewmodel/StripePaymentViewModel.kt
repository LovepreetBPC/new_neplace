package com.example.neplacecustomer.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.AddRidesModel
import com.example.neplacecustomer.model.StripePaymentModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class StripePaymentViewModel : ViewModel() {
    var stripePaymentResponse: MutableLiveData<BaseResponse<StripePaymentModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun addStripePayment(body: MultipartBody) {
        stripePaymentResponse.value=null
        stripePaymentResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.stripePayment(body)

                if (response.code() == 200) {
                    stripePaymentResponse.value = null
                    stripePaymentResponse.value = BaseResponse.Success(response.body())
                } else {
                    stripePaymentResponse.value = null
                    stripePaymentResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
                Log.e("TAG_StripePayment", "addStripePayment: "+ae.message)

            }

        }

    }

}