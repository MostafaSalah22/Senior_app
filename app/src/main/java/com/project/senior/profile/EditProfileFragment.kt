package com.project.senior.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.project.senior.R
import com.project.senior.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentEditProfileBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgEditProfile.load(R.drawable.me){
            transformations(CircleCropTransformation())
        }

       clickListener()
    }

    private fun clickListener() {
        binding.imgBackEditProfile.setOnClickListener {
            backToProfileFragment()
        }

        binding.btnSaveEditProfile.setOnClickListener {
            backToProfileFragment()
        }

        binding.btnCancelEditProfile.setOnClickListener {
            backToProfileFragment()
        }
    }


    private fun backToProfileFragment() {
        findNavController().popBackStack()
    }


}