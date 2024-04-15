package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.GetCards
import com.example.neplacecustomer.model.UserNotificationModel
import kotlinx.coroutines.launch

class GetCardVM : ViewModel() {
    var getCardsResponse: MutableLiveData<BaseResponse<GetCards>?> = MutableLiveData()
    private val getCardsRepo = UserRepository()

    fun getCard() {
        getCardsResponse.value=null
        getCardsResponse.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = getCardsRepo.getCardsRepo()
                if (response.code() == 200) {
                    getCardsResponse.value=null
                    getCardsResponse.value = BaseResponse.Success(response.body())
                } else {
                    getCardsResponse.value=null
                    getCardsResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}