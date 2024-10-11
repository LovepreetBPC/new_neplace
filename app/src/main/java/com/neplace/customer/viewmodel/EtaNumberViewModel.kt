package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.AirlineModel
import com.neplace.customer.model.EtaNumberModel
import kotlinx.coroutines.launch


class EtaNumberViewModel : ViewModel() {
    var etaNumberResponse: MutableLiveData<BaseResponse<EtaNumberModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getEtaNumber(key:String) {
        etaNumberResponse.value=null
        etaNumberResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getEtaNumber(key)
                if (response.code() == 200) {
                    etaNumberResponse.value=null
                    etaNumberResponse.value = BaseResponse.Success(response.body())
                } else {
                    etaNumberResponse.value=null
                    etaNumberResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}