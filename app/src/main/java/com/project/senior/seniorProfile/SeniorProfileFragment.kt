package com.project.senior.seniorProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentSeniorProfileBinding
import com.project.senior.seniorDetails.SeniorDetailsFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeniorProfileFragment : Fragment() {

    private lateinit var binding:FragmentSeniorProfileBinding
    private val viewModel: SeniorProfileViewModel by viewModels()
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSeniorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileFragmentArgs by navArgs<SeniorProfileFragmentArgs>()
        userId = profileFragmentArgs.userId
        lifecycleScope.launchWhenCreated {
            viewModel.getSeniorProfile(userId!!)
        }
        viewModel.getSeniorProfileResponseState.observe(viewLifecycleOwner, Observer { state->
            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }
        })
        clickListeners()
    }

    private fun clickListeners() {
        binding.imgBackSeniorProfile.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun successState() {
        viewModel.seniorProfile.observe(viewLifecycleOwner, Observer { data ->

            binding.tvNameSeniorProfile.text = data.name
            binding.tvNameSenior.text = data.name
            binding.tvPhoneSenior.text = data.phone
            binding.tvBirthdateSenior.text = data.birthdate
            binding.tvAgeSenior.text = data.age.toString()
            binding.imgSeniorProfile.load(data.image){
                placeholder(R.drawable.loading_img)
                transformations(CircleCropTransformation())
            }

        })

        binding.groupSeniorProfile.visibility = View.VISIBLE
        binding.progressBarSeniorProfile.visibility = View.GONE
    }

    private fun loadingState() {
        binding.groupSeniorProfile.visibility = View.GONE
        binding.progressBarSeniorProfile.visibility = View.VISIBLE
    }

    private fun errorState() {
        binding.groupSeniorProfile.visibility = View.VISIBLE
        binding.progressBarSeniorProfile.visibility = View.GONE
        Snackbar.make(requireView(), "Something error!",Snackbar.LENGTH_LONG).show()
    }

}