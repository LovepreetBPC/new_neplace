package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.AirPortCodeModel
import com.example.neplacecustomer.model.ProfileModel
import kotlinx.coroutines.launch


class AirPortCodeViewModel : ViewModel() {
    var airPortCodeResponse: MutableLiveData<BaseResponse<AirPortCodeModel>?> = MutableLiveData()
    val userRepo = UserRepository()
    fun getAirPortCode() {
        airPortCodeResponse.value=null
        airPortCodeResponse.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = userRepo.airPortCode()
                if (response.code() == 200) {
                    airPortCodeResponse.value=null
                    airPortCodeResponse.value = BaseResponse.Success(response.body())
                } else {
                    airPortCodeResponse.value=null
                    airPortCodeResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}