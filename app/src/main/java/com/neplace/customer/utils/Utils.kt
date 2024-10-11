package com.neplace.customer.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

object Utils {


    const val BASE_URL = "https://dev.pricepally.com/api/"

    var stripeToken = ""


    // headers
    const val HEADERS = "Content-Type"
    const val AUTH_HEADERS = "Authorization"
    const val header = "application/json"
    const val DeviceType = "android"


    fun saveApiKeys(context: Context, googleKey: String, mapboxKey: String, stripeKey: String) {
        val sharedPreferences = context.getSharedPreferences("API_KEYS", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("MAPS_API_KEY", googleKey)
        editor.putString("MAPBOX_API_KEY", mapboxKey)
        editor.putString("STRIPE_KEY", stripeKey)

        editor.apply() // Save changes
    }

    fun getApiKeys(context: Context): Map<String, String?> {
        val sharedPreferences = context.getSharedPreferences("API_KEYS", Context.MODE_PRIVATE)

        val googleKey = sharedPreferences.getString("MAPS_API_KEY", null)
        val mapboxKey = sharedPreferences.getString("MAPBOX_API_KEY", null)
        val stripeKey = sharedPreferences.getString("STRIPE_KEY", null)

        stripeToken = stripeKey ?: "pk_test_51NpyRGEPE4f5OLIc0orSy6NLsGo17NcsXQ9khzmSx94iOidaIVgkoPpT5dPsFtEORp3svJQkrbn2LCE0Ohz7gY1w00UpdKzon0"

        return mapOf(
            "MAPS_API_KEY" to googleKey,
            "MAPBOX_API_KEY" to mapboxKey,
            "STRIPE_KEY" to stripeKey
        )
    }

    fun hasInternetConnection(application: MyApplication): Boolean {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun showNoInternetDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("No Internet Connection")
        builder.setMessage("Please check your internet connection and try again.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

}