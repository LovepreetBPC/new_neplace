package com.example.neplacecustomer.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import com.example.neplacecustomer.common.Constant
import com.example.neplacecustomer.common.SharedPref
import com.example.neplacecustomer.utils.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object UserNetwork {
var sharePref= SharedPref
    private lateinit var retrofitStripe: Retrofit
    private lateinit var REST_CLIENT: ApiInterface


    val retrofit: ApiInterface by lazy {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .addNetworkInterceptor(header!!)

            .connectTimeout(50, TimeUnit.MINUTES)
            .writeTimeout(50, TimeUnit.MINUTES)
            .readTimeout(50, TimeUnit.MINUTES)

            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(Constant.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiInterface::class.java)

    }


    private var header: Interceptor? = Interceptor { chain ->

        val newRequest: Request = chain.request().newBuilder()
//            .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5OGZiYTM5NS0yZTM5LTQwM2MtOTg3OS0xMTkxYTkyMmQzMjEiLCJqdGkiOiI1M2M0YjljZDA2OWYyNzNlMjQ2M2I2MmE1N2JlMTFlMjNjOTkzNzc1YjNkZGRkMWY0OGZhNmIzMWMxMjUzOWQxYWZiNjViNzM3YTZiYmY3NCIsImlhdCI6MTY5NjMyNzM0Mi4wNjk3MTksIm5iZiI6MTY5NjMyNzM0Mi4wNjk3MjMsImV4cCI6MTcyNzk0OTc0Mi4wNjM5NTUsInN1YiI6IjE1MyIsInNjb3BlcyI6W119.Yaydu6st7IQVa165p8e3aO_Qrr9GRCDpXzZnBJYB3tBB_cQDxCyALfdblsN0FzqO3td8vdSR_mQlDZWyU9v0mBb8X80Cq2QNGJf6ltkKb0cL9UOsXf1qqaoBwxnfWTgV4jkXZqtAOct1ic74IN1aqoeix2_nkSZ5K0sMnaEEax0X7G9VyrkOonS4B6004w07YX766pFkhCcbYw8F5sGjNQXo3qvJgOBhMBwbm9JPqhWlGf5dtKiUkArFQr--PCklf-x59aIOEym6YvOLEwoAt08HqWEbMsN_4rqc56OdiSGQ1-v2wm9Sf1S3bIFMyK_KroIy9ke8Y-iU32MM3X1IXx0P7GmtytK_DZ4sVK6qtmz8uT8nQID4MhxjNxrTsrbzhoYNowRZeB9H7nSvrYfZ3WMWL-UxRg34Tgy2lFSiVZfp5b6MY7lbeM27tO90QiXek903vjE5rZgFQxLFYvBshacx_OyG8HD6BjtE47soPzjNJ08nmfM2Oc31fIdmiX1Xrlwgg6G2siOlpjv_8ACBCv8yy6GjD_TRxWcdiiezcQiEtPZPl0wSkH2Vsv-UAX_fq4C5hI_BxNUxiHq3QZkaj4OrXjghI-D72JDug6nXICQMo-j1nXYGoN3tC9ajC1HfdVNaEvTuK1xViDul4aU_y9s8TGDutYKu5OO4-t88IvA")
            .header("Authorization", "Bearer ${sharePref.getString(Constant.TOKEN, "").toString()}")
            .build()

        Log.e("access_token", "token: "+ sharePref.getString(Constant.TOKEN, "").toString() )
        chain.proceed(newRequest)

    }





    fun getStripe(): ApiInterface {
        retrofitStripe = Retrofit.Builder().baseUrl("https://api.stripe.com/v1/").client(
            getOkHttpClient12()
        ).addConverterFactory(GsonConverterFactory.create()).build()
        REST_CLIENT = retrofitStripe.create(ApiInterface::class.java)
        return REST_CLIENT
    }

    fun getOkHttpClient12(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(5, TimeUnit.MINUTES)
        builder.readTimeout(5, TimeUnit.MINUTES)
        builder.writeTimeout(5, TimeUnit.MINUTES)
        builder.addNetworkInterceptor(httpLoggingInterceptor)
        builder.protocols(listOf(Protocol.HTTP_1_1))
        builder.addInterceptor { chain ->
            val request = chain.request()
            val header = request.newBuilder()
                .header("Authorization", "Bearer ${Utils.stripeToken}")
            val build = header!!.build()
            chain.proceed(build)
        }
        return builder.build()
    }







}