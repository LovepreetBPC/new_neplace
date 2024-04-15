package com.example.neplacecustomer.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.SendRatingModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class SendRatingViewModel : ViewModel() {
    var addRidesResponse: MutableLiveData<BaseResponse<SendRatingModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun senReview(body: MultipartBody) {
        addRidesResponse.value=null
        addRidesResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.sendRating(body)
                if (response.code() == 200) {
                    addRidesResponse.value=null

                    addRidesResponse.value = BaseResponse.Success(response.body())
                } else {
                    addRidesResponse.value=null

                    addRidesResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()

                Log.e("senReview", "senReview: "+ae.message.toString())
            }

        }

    }

}