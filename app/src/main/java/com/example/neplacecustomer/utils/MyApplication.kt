package com.example.neplacecustomer.utils

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.neplacecustomer.utils.Utils.showNoInternetDialog
import com.google.firebase.FirebaseApp

class MyApplication: Application(){
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}