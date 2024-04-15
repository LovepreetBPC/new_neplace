package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.GetRideModel
import kotlinx.coroutines.launch


class CancelRideViewModel : ViewModel() {
    var cancelRideResponse: MutableLiveData<BaseResponse<GetRideModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun cancelRide(id:String) {
        cancelRideResponse.value=null
        cancelRideResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.cancelRides(id)
                if (response.code() == 200) {
                    cancelRideResponse.value=null
                    cancelRideResponse.value = BaseResponse.Success(response.body())
                } else {
                    cancelRideResponse.value=null
                    cancelRideResponse.value = BaseResponse.Error(response.message())
                }
            } catch (ae: Exception) {
                ae.printStackTrace()
            }
        }
    }
}