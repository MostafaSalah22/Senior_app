package com.project.senior.first

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.senior.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private val viewModel: FirstViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentFirstBinding.inflate(inflater,container,false)
        lifecycleScope.launchWhenCreated {
            if (viewModel.isEmailLoggedIn()) navigateToLoginFragment()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListener()
    }

    private fun clickListener() {
        binding.cardDoctorFirst.setOnClickListener {
            runBlocking {
                viewModel.setUserType("doctor")
            }
            navigateToLoginFragment()
        }

        binding.cardUserFirst.setOnClickListener {
            runBlocking {
                viewModel.setUserType("user")
            }
            navigateToLoginFragment()
        }
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToLoginFragment())
    }

}