package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.AirlineModel
import com.neplace.customer.model.FlightNumberModel
import kotlinx.coroutines.launch


class FlightNumberViewModel : ViewModel() {
    var flightNumberResponse: MutableLiveData<BaseResponse<FlightNumberModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getFlightNumber(key:String) {
        flightNumberResponse.value=null
        flightNumberResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getFlightNumber(key)
                if (response.code() == 200) {
                    flightNumberResponse.value=null
                    flightNumberResponse.value = BaseResponse.Success(response.body())
                } else {
                    flightNumberResponse.value=null
                    flightNumberResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}