package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.GetRideModel
import kotlinx.coroutines.launch


class GetRideDetailViewModel : ViewModel() {
    var getRideResponse: MutableLiveData<BaseResponse<GetRideModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getRide(id:String) {
        getRideResponse.value=null
        getRideResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getRides(id)
                if (response.code() == 200) {
                    getRideResponse.value=null
                    getRideResponse.value = BaseResponse.Success(response.body())
                } else {
                    getRideResponse.value=null
                    getRideResponse.value = BaseResponse.Error(response.message())
                }
            } catch (ae: Exception) {
                ae.printStackTrace()
            }
        }
    }
}