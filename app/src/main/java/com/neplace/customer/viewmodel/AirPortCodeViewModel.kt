package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.AirPortCodeModel
import com.neplace.customer.model.ProfileModel
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