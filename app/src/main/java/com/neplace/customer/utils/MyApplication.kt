package com.neplace.customer.utils

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.neplace.customer.utils.Utils.showNoInternetDialog
import com.google.firebase.FirebaseApp

class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}