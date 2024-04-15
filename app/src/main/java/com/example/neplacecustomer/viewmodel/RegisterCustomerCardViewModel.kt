package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.model.VerifyOtpModel
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class RegisterCustomerCardViewModel : ViewModel() {
    var registerCustomerCardResponse: MutableLiveData<BaseResponse<VerifyOtpModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun registerCustomerCardUser(front_image: MultipartBody.Part, back_image:MultipartBody.Part) {
        registerCustomerCardResponse.value=null
        registerCustomerCardResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.registerCustomerCard( front_image, back_image)

                if (response.code() == 200) {
                    registerCustomerCardResponse.value=null

                    registerCustomerCardResponse.value = BaseResponse.Success(response.body())
                } else {
                    registerCustomerCardResponse.value=null

                    registerCustomerCardResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}