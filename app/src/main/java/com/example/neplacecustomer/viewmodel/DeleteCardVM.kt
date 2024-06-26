package com.example.neplacecustomer.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.DeleteCardModel
import com.example.neplacecustomer.model.UserNotificationModel
import kotlinx.coroutines.launch

class DeleteCardVM: ViewModel() {
    var deleteCard: MutableLiveData<BaseResponse<DeleteCardModel>?> = MutableLiveData()
    val delCardRepo = UserRepository()

    fun deletecard(card_id:String) {
        deleteCard.value=null
        deleteCard.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = delCardRepo.deleteSingleCard(card_id)
                if (response.code() == 200) {
                    deleteCard.value=null
                    deleteCard.value = BaseResponse.Success(response.body())
                    Log.e("deletecard", "deletecard: "+response.message() )
                } else {
                    deleteCard.value=null
                    deleteCard.value = BaseResponse.Error(response.message())
                    Log.e("deletecard", "deletecard: error"+deleteCard.value )

                }

            } catch (ae: Exception) {
                ae.printStackTrace()
                Log.e("deletecard", "deletecard: error"+ae.message )

            }

        }

    }

}