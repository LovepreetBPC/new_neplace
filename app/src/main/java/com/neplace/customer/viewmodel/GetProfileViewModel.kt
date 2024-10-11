package com.neplace.customer.viewmodel

import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.ProfileModel
import kotlinx.coroutines.launch


class GetProfileViewModel : ViewModel() {
    var profileEditResponse: MutableLiveData<BaseResponse<ProfileModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getProfileUser() {
        profileEditResponse.value=null
        profileEditResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.getProfile()
                if (response.code() == 200) {
                    profileEditResponse.value=null

                    profileEditResponse.value = BaseResponse.Success(response.body())
                } else {
                    profileEditResponse.value=null

                    profileEditResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()

            }

        }

    }

}