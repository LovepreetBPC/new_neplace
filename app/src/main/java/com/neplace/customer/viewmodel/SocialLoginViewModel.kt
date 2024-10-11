package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.SocialLoginModel
import kotlinx.coroutines.launch


class SocialLoginViewModel : ViewModel() {
    var loginResponse: MutableLiveData<BaseResponse<SocialLoginModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun loginUser(email: String, device_token: String, device_type: String, role_id: String, google_id: String) {
        loginResponse.value=null
        loginResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.socialLogin(email, device_token, device_type, role_id,google_id)

                if (response.code() == 200) {
                    loginResponse.value=null

                    loginResponse.value = BaseResponse.Success(response.body())
                } else {
                    loginResponse.value=null

                    loginResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}