package com.nexter.application.retrofit

import com.neplace.customer.retrofit.ApiInterface
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


object RetrofitUtils {

    private const val MULTIPART_FORM_DATA = "multipart/form-data"


    var MAPS_API_KEY: String? = ""

    fun stringToRequestBody(string: String): RequestBody {
        return string.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
    }

    fun intToRequestBody(string: String): RequestBody {
        return string.toRequestBody(
            "text/plain".toMediaTypeOrNull()
        )
    }

    fun intRequest(string: Int): RequestBody {
        return Gson().toJson(string)
            .toRequestBody(MultipartBody.FORM)
    }


    fun imageToRequestBody(file: File): RequestBody {
        return file.asRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
    }

    fun createPartFromFile(partName: String, file: File): MultipartBody.Part {
        // create RequestBody instance from file
        val requestFile = file.asRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


    private const val BASE_URL = "https://api.mapbox.com/"

    val instance: ApiInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiInterface::class.java)
    }

}