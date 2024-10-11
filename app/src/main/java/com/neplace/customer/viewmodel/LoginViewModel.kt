package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.model.LoginModel
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    var loginResponse: MutableLiveData<BaseResponse<LoginModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun loginUser(email: String, ) {
        loginResponse.value=null
        loginResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.loginUser(email)

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