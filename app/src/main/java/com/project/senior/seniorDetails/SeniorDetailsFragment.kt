package com.project.senior.seniorDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.senior.R
import com.project.senior.databinding.FragmentSeniorDetailsBinding
import com.project.senior.databinding.FragmentSeniorsBinding


class SeniorDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSeniorDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSeniorDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListener()
    }

    private fun clickListener() {
        binding.cardScheduleSeniorDetails.setOnClickListener {
            val detailsFragmentArgs by navArgs<SeniorDetailsFragmentArgs>()
            val userId = detailsFragmentArgs.userId
            navigateToScheduleFragment(userId)
        }

        binding.imgBackSeniorDetails.setOnClickListener {
            backToSeniorsFragment()
        }
    }

    private fun navigateToScheduleFragment(userId: Int) {
        findNavController().navigate(SeniorDetailsFragmentDirections.actionSeniorDetailsFragmentToScheduleFragment(userId))
    }

    private fun backToSeniorsFragment() {
        findNavController().popBackStack()
    }

}