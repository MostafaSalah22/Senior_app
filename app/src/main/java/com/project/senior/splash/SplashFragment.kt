package com.project.senior.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.senior.R


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Handler().postDelayed({
            lifecycleScope.launchWhenResumed {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragment2ToFirstFragment2())
            }
        }, 1700)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}