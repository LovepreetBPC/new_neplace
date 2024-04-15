package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.GetPlanModel
import com.example.neplacecustomer.model.PlanDetailModel
import com.example.neplacecustomer.model.ProfileModel
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