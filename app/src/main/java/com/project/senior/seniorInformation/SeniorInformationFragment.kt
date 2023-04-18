package com.project.senior.seniorInformation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.project.senior.R
import com.project.senior.databinding.FragmentSeniorDetailsBinding
import com.project.senior.databinding.FragmentSeniorInformationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeniorInformationFragment : Fragment() {

    private lateinit var binding: FragmentSeniorInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSeniorInformationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListener()
    }

    private fun clickListener() {
        binding.imgBackSeniorInformation.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}