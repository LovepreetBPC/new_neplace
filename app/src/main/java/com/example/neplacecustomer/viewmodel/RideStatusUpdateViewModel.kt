package com.example.neplacecustomer.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.RideStatusUpdateModel
import kotlinx.coroutines.launch


class RideStatusUpdateViewModel : ViewModel() {
    var rideResponse: MutableLiveData<BaseResponse<RideStatusUpdateModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun updateRideStatus(status:String , user_id : String,is_reject_driver:Boolean ){
        rideResponse.value=null
        rideResponse.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = userRepo.rideStatusUpdate(status,user_id,is_reject_driver)
                if (response.code() == 200) {
                    rideResponse.value=null
                    rideResponse.value = BaseResponse.Success(response.body())
                } else {
                    rideResponse.value=null
                    rideResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()

                Log.e("UpdateStatus", "getRide: "+ae.message)
            }

        }

    }

}