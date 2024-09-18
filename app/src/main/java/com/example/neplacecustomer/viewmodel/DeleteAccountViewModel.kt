package com.example.neplacecustomer.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neplacecustomer.login.repository.BaseResponse
import com.example.neplacecustomer.login.repository.UserRepository
import com.example.neplacecustomer.model.DeleteUserAccountModel
import kotlinx.coroutines.launch

class DeleteAccountViewModel : ViewModel() {
    val successReponse: MutableLiveData<BaseResponse<DeleteUserAccountModel>> = MutableLiveData()
    val userRepo = UserRepository()

    fun deleteAccount(
        user_id: String
    ) {
        successReponse.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = userRepo.deleteAccount(user_id)

                if (response.isSuccessful) {
                    successReponse.value = BaseResponse.Success(response.body())
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    successReponse.value = BaseResponse.Error(errorMessage)
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
                Log.e("updateVehicleError", "updateVehicle: ${ae.message}", ae)
                successReponse.value = BaseResponse.Error(ae.message ?: "An error occurred")
            }
        }
    }
}
