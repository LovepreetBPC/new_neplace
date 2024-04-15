package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.model.LoginModel
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
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