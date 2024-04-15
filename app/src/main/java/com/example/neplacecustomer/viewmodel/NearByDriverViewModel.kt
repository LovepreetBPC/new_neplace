package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.GetNearByDriverModel
import kotlinx.coroutines.launch


class NearByDriverViewModel : ViewModel() {
    var nearByDriverResponse: MutableLiveData<BaseResponse<GetNearByDriverModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun nearByDriver(lat: String, long:String,vehicle_type:String) {
        nearByDriverResponse.value=null
        nearByDriverResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getNearByDriver( lat,long,vehicle_type)
                if (response.code() == 200) {
                    nearByDriverResponse.value=null

                    nearByDriverResponse.value = BaseResponse.Success(response.body())
                } else {
                    nearByDriverResponse.value=null

                    nearByDriverResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}