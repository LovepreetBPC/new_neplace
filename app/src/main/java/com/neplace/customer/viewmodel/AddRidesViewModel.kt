package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.AddRidesModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class AddRidesViewModel : ViewModel() {
    var addRidesResponse: MutableLiveData<BaseResponse<AddRidesModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getRidesUser(body: MultipartBody) {
        addRidesResponse.value=null
        addRidesResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.addRides(body)
                if (response.code() == 200) {
                    addRidesResponse.value=null

                    addRidesResponse.value = BaseResponse.Success(response.body())
                } else {
                    addRidesResponse.value=null

                    addRidesResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
            }

        }

    }

}