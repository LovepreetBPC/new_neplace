package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.PlanSubscripationModel
import kotlinx.coroutines.launch


class PlanSubscriptionViewModel : ViewModel() {
    var planSubscriptionResponse: MutableLiveData<BaseResponse<PlanSubscripationModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getPlanSubscription(plan_id: String,card_id: String) {
        planSubscriptionResponse.value=null
        planSubscriptionResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.planSubscription(plan_id, card_id)
                if (response.code() == 200) {
                    planSubscriptionResponse.value=null
                    planSubscriptionResponse.value = BaseResponse.Success(response.body())
                } else {
                    planSubscriptionResponse.value=null
                    planSubscriptionResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}