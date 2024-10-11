package com.neplace.customer.model

import com.google.gson.annotations.SerializedName

data class DeleteUserAccountModel(
    @SerializedName("status"     ) var status     : Boolean? = null,
    @SerializedName("statusCode" ) var statusCode : Int?     = null,
    @SerializedName("message"    ) var message    : String?  = null,
    // @SerializedName("data"       ) var data       : DeleteData?    = DeleteData()
)
