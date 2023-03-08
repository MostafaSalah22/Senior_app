package com.project.senior.signup

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.MainActivity
import com.project.senior.R
import com.project.senior.databinding.FragmentSignupBinding
import com.project.senior.login.LoginViewModel
import com.project.senior.utils.showPassword
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSignupBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgShowPassSignup.tag = R.drawable.baseline_visibility_off_24
        binding.imgShowConfirmPassSignup.tag = R.drawable.baseline_visibility_off_24
        clickListener()
    }

    private fun clickListener() {
        binding.btnLoginSignup.setOnClickListener {
            backToLoginFragment()
        }

        binding.imgShowPassSignup.setOnClickListener {
            showPassword(binding.etPasswordSignup, binding.imgShowPassSignup)
        }

        binding.imgShowConfirmPassSignup.setOnClickListener {
            showPassword(binding.etConfirmPasswordSignup, binding.imgShowConfirmPassSignup)
        }

        binding.btnSignup.setOnClickListener {
            if((requireActivity() as MainActivity).isInternetAvailable) {
                lifecycleScope.launchWhenCreated {
                    postRegisterUser()

                    viewModel.responseState.observe(viewLifecycleOwner, Observer { state ->
                        when (state) {
                            is Resource.Success -> successState()
                            is Resource.Loading -> loadingState()
                            is Resource.Error -> errorState()
                            else -> Log.i("LoginViewModel", "ERROR")
                        }
                    })
                }
            }
            else Snackbar.make(requireView(), "Check the internet and try again.", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun backToLoginFragment() {
        findNavController().popBackStack()
    }

    private fun postRegisterUser() {
        viewModel.postRegisterUser(
            binding.etUsernameSignup.text.toString().trim(),
            binding.etNameSignup.text.toString().trim(),
            binding.etPasswordSignup.text.toString().trim(),
            binding.etConfirmPasswordSignup.text.toString().trim(),
            binding.etPhoneSignup.text.toString().trim(),
            binding.etEmailSignup.text.toString().trim()
        )
    }

    private fun successState() {
        backToLoginFragment()
        Snackbar.make(requireView(),"Email accepted.", Snackbar.LENGTH_LONG).show()
    }

    private fun loadingState() {
        binding.groupSignup.visibility = View.GONE
        binding.progressBarSignup.visibility = View.VISIBLE
    }

    private fun errorState() {
        binding.progressBarSignup.visibility = View.GONE
        binding.groupSignup.visibility = View.VISIBLE
        viewModel.signupUser.observe(viewLifecycleOwner, Observer { userResponse ->
            if(userResponse.status == "E03" || userResponse.status == "E00") binding.tvErrorSignup.text = userResponse.message
            else postRegisterUser()
        })
    }
}