package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.GetVehicleModel
import kotlinx.coroutines.launch


class GetVehicleViewModel : ViewModel() {
    var getVehicleResponse: MutableLiveData<BaseResponse<GetVehicleModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getVehicleUser(vehicle_id:String) {
        getVehicleResponse.value=null
        getVehicleResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getVehicleType()
                if (response.code() == 200) {
                    getVehicleResponse.value=null

                    getVehicleResponse.value = BaseResponse.Success(response.body())
                } else {
                    getVehicleResponse.value=null

                    getVehicleResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}