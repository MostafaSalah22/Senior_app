package com.project.senior.bookingDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.senior.R
import com.project.senior.databinding.FragmentBookingDetailsBinding
import com.project.senior.seniorDetails.SeniorDetailsFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingDetailsFragment : Fragment() {

    private lateinit var binding: FragmentBookingDetailsBinding
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
                FragmentBookingDetailsBinding.inflate(inflater, container, false)
        val bookingDetailsFragmentArgs by navArgs<BookingDetailsFragmentArgs>()
        userId = bookingDetailsFragmentArgs.userId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListener()
    }

    private fun clickListener() {
        binding.imgBackBookingDetails.setOnClickListener {
            backToBookingsFragment()
        }

        binding.cardInformationBookingDetails.setOnClickListener {
            navigateToBookingInformationFragment(userId!!)
        }

        binding.cardMedicinesBookingDetails.setOnClickListener {
            navigateToMedicinesFragment(userId!!)
        }
    }

    private fun backToBookingsFragment() {
        findNavController().popBackStack()
    }

    private fun navigateToBookingInformationFragment(userId: Int){
        findNavController().navigate(BookingDetailsFragmentDirections.actionBookingDetailsFragmentToBookingInformationFragment(userId))
    }

    private fun navigateToMedicinesFragment(userId: Int){
        findNavController().navigate(BookingDetailsFragmentDirections.actionBookingDetailsFragmentToMedicinesFragment(userId))
    }
}