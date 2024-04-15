package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.AirlineModel
import com.example.neplacecustomer.model.EtaNumberModel
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