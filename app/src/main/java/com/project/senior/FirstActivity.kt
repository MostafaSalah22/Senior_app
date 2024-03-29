package com.project.senior

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstActivity : AppCompatActivity() {

    var isInternetAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        checkInternetConnectivity()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun checkInternetConnectivity() {
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
    }
}