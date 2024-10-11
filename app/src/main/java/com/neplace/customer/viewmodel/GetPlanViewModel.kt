package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.GetPlanModel
import com.neplace.customer.model.ProfileModel
import kotlinx.coroutines.launch


class GetPlanViewModel : ViewModel() {
    var planResponse: MutableLiveData<BaseResponse<GetPlanModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getPlan() {
        planResponse.value=null
        planResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getPlans()
                if (response.code() == 200) {
                    planResponse.value=null
                    planResponse.value = BaseResponse.Success(response.body())
                } else {
                    planResponse.value=null
                    planResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}