package com.example.neplacecustomer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.AddCardModel
import kotlinx.coroutines.launch

class AddCardViewModel : ViewModel() {
    var addCardDetail: MutableLiveData<BaseResponse<AddCardModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun addCard(hashMap: HashMap<String, Any>) {
        addCardDetail.value = null
        addCardDetail.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = userRepo.addCardRepo(hashMap)
                if (response.code() == 200) {
                    addCardDetail.value = null
                    addCardDetail.value = BaseResponse.Success(response.body())
                } else {
                    addCardDetail.value = null
                    addCardDetail.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}