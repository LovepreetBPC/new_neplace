package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.model.VerifyOtpModel
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class RegisterViewModel : ViewModel() {
    var registerResponse: MutableLiveData<BaseResponse<VerifyOtpModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun registerUser(first_name: String, last_name:String, email:String, phone_number:String, role_id:String, city:String, address:String, password:String, password_confirmation:String, image: MultipartBody.Part, device_token:String  ) {
        registerResponse.value=null
        registerResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.registerUser(first_name,last_name, email,phone_number, role_id, city, address, password, password_confirmation, image,device_token)

                if (response.code() == 200) {
                    registerResponse.value=null

                    registerResponse.value = BaseResponse.Success(response.body())
                } else {
                    registerResponse.value=null

                    registerResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}