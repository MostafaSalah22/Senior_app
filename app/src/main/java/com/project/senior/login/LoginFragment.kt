package com.project.senior.login

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
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentLoginBinding
import com.project.senior.utils.showPassword
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentLoginBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgShowPassLogin.tag = R.drawable.baseline_visibility_off_24
        clickListener()
    }

    private fun clickListener() {
        binding.btnSignupLogin.setOnClickListener {
            navigateToSignupFragment()
        }

        binding.imgShowPassLogin.setOnClickListener {
            showPassword(binding.etPasswordLogin, binding.imgShowPassLogin)
        }

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                postLoginUser()

                viewModel.responseState.observe(viewLifecycleOwner, Observer {state ->
                    when(state){
                        is Resource.Success -> successState()
                        is Resource.Loading -> loadingState()
                        is Resource.Error -> errorState()
                        else -> Log.i("LoginViewModel", "ERROR")
                    }
                })
            }
        }
    }

    private fun navigateToSignupFragment() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
    }

    private fun navigateToChatFragment() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChatFragment())
    }

    private fun postLoginUser(){
        viewModel.postLoginUser(
            binding.etUsernameLogin.text.toString().trim(),
            binding.etPasswordLogin.text.toString().trim()
        )
    }

    private fun successState() = navigateToChatFragment()

    private fun loadingState() {
        binding.groupLogin.visibility = View.GONE
        binding.progressBarLogin.visibility = View.VISIBLE
    }

    private fun errorState() {
        binding.progressBarLogin.visibility = View.GONE
        binding.groupLogin.visibility = View.VISIBLE
        viewModel.loginUser.observe(viewLifecycleOwner, Observer { userResponse ->
            if(userResponse.status == "E03" || userResponse.status == "E00") binding.tvErrorLogin.text = userResponse.message
            else postLoginUser()
        })
    }
}