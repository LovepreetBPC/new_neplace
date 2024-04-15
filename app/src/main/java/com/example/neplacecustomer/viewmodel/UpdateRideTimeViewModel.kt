package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.AddRidesModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class UpdateRideTimeViewModel : ViewModel() {
    var updateRidesResponse: MutableLiveData<BaseResponse<AddRidesModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun updateRidesUser(body: MultipartBody) {
        updateRidesResponse.value=null
        updateRidesResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.rideTimeUpdate(body)
                if (response.code() == 200) {
                    updateRidesResponse.value=null

                    updateRidesResponse.value = BaseResponse.Success(response.body())
                } else {
                    updateRidesResponse.value=null

                    updateRidesResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}