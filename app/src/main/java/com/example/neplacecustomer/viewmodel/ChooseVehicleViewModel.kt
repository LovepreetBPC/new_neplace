package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.ChooseVehicleModel
import kotlinx.coroutines.launch


class ChooseVehicleViewModel : ViewModel() {
    var profileEditResponse: MutableLiveData<BaseResponse<ChooseVehicleModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getVehicleUser(vehicle_id:String) {
        profileEditResponse.value=null
        profileEditResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.chooseVehicle(vehicle_id)
                if (response.code() == 200) {
                    profileEditResponse.value=null

                    profileEditResponse.value = BaseResponse.Success(response.body())
                } else {
                    profileEditResponse.value=null

                    profileEditResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}