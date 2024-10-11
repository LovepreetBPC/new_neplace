package com.neplace.customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.GetCards
import com.neplace.customer.model.UserNotificationModel
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