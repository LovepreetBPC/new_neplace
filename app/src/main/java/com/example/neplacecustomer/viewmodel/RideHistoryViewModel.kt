package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.RideHistoryModel
import kotlinx.coroutines.launch


class RideHistoryViewModel : ViewModel() {
    var rideHistoryResponse: MutableLiveData<BaseResponse<RideHistoryModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getRideHistory() {
        rideHistoryResponse.value=null
        rideHistoryResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.userRidesHistory()
                if (response.code() == 200) {
                    rideHistoryResponse.value=null
                    rideHistoryResponse.value = BaseResponse.Success(response.body())
                } else {
                    rideHistoryResponse.value=null
                    rideHistoryResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}