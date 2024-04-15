package com.example.neplacecustomer.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.ProfileModel
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