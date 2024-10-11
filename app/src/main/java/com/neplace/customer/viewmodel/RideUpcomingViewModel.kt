package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.RideHistoryModel
import kotlinx.coroutines.launch


class RideUpcomingViewModel : ViewModel() {
    var rideUpcomingResponse: MutableLiveData<BaseResponse<RideHistoryModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getRide(status:String) {
        rideUpcomingResponse.value=null
        rideUpcomingResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getRide(status)
                if (response.code() == 200) {
                    rideUpcomingResponse.value=null
                    rideUpcomingResponse.value = BaseResponse.Success(response.body())
                } else {
                    rideUpcomingResponse.value=null
                    rideUpcomingResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}