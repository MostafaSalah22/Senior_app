package com.project.senior

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.domain.usecase.DataStoreTypeUseCase
import com.project.senior.chat.ChatFragment
import com.project.senior.databinding.ActivityMainBinding
import com.project.senior.profile.ProfileFragment
import com.project.senior.seniors.SeniorsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var typeUseCase: DataStoreTypeUseCase

    private lateinit var binding: ActivityMainBinding
    var isInternetAvailable = false
    private lateinit var type: String
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkInternetConnectivity()

        runBlocking {
            type = if (typeUseCase.getType() == "doctor"){
                "doctor"
            } else "user"
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        if(type == "doctor"){
            val menu: Menu = bottomNavigationView.menu
            menu.removeItem(R.id.seniorsFragment)
        }
        else {
            val menu: Menu = bottomNavigationView.menu
            menu.removeItem(R.id.bookingsFragment)
        }

        binding.bottomNavigation.setupWithNavController(navController)

    }

    private fun checkInternetConnectivity() {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onAvailable(network: Network) {
                isInternetAvailable = true
            }

            override fun onLost(network: Network) {
                isInternetAvailable = false
            }
        }
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
    }

    /*override fun onBackPressed() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        if (bottomNavigationView.selectedItemId == R.id.chatFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }*/
}