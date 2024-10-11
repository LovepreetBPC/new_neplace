package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.AirlineModel
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