package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.AirlineModel
import kotlinx.coroutines.launch


class AirlineViewModel : ViewModel() {
    var airlineResponse: MutableLiveData<BaseResponse<AirlineModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getAirLine() {
        airlineResponse.value=null
        airlineResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getAirline()
                if (response.code() == 200) {
                    airlineResponse.value=null
                    airlineResponse.value = BaseResponse.Success(response.body())

                } else {
                    airlineResponse.value=null
                    airlineResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}