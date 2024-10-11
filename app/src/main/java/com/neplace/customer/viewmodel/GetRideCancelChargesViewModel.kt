package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.GetRideModel
import com.neplace.customer.model.RideCancelChargesModel
import kotlinx.coroutines.launch


class GetRideCancelChargesViewModel : ViewModel() {
    var getRideCancelChargesResponse: MutableLiveData<BaseResponse<RideCancelChargesModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getRideCancelCharges(id:String) {
        getRideCancelChargesResponse.value=null
        getRideCancelChargesResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getRideCancelCharges(id)
                if (response.code() == 200) {
                    getRideCancelChargesResponse.value=null
                    getRideCancelChargesResponse.value = BaseResponse.Success(response.body())
                } else {
                    getRideCancelChargesResponse.value=null
                    getRideCancelChargesResponse.value = BaseResponse.Error(response.message())
                }
            } catch (ae: Exception) {
                ae.printStackTrace()
            }
        }
    }
}