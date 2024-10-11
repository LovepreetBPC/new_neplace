package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.GetPlanModel
import com.neplace.customer.model.PlanDetailModel
import com.neplace.customer.model.ProfileModel
import kotlinx.coroutines.launch


class GetPlanDetailViewModel : ViewModel() {
    var planResponse: MutableLiveData<BaseResponse<PlanDetailModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getPlan(id:String) {
        planResponse.value=null
        planResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.plansDetail(id)
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