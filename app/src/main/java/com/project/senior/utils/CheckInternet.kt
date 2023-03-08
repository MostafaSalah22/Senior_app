package com.project.senior.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

 fun isInternetAvailable(application:Application): Boolean {
     var isInternetAvailable = false
    val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onAvailable(network: Network) {
            Log.i("LoginViewModel", "onAvailable: ")
            isInternetAvailable = true
        }

        override fun onLost(network: Network) {
            Log.i("LoginViewModel", "onLost: ")
            isInternetAvailable = false
        }
    }
    connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)

     return isInternetAvailable
 }