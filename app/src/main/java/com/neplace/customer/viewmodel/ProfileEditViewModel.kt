package com.neplace.customer.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.neplace.customer.login.repository.BaseResponse
import com.neplace.customer.login.repository.UserRepository
import com.neplace.customer.model.ProfileModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class ProfileEditViewModel : ViewModel(){
    var profileEditResponse: MutableLiveData<BaseResponse<ProfileModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun profileEditUser(first_name: String, last_name:String, image: MultipartBody.Part,) {
        profileEditResponse.value=null
        profileEditResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.profileEdit( first_name, last_name, image)
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