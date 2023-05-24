package com.project.senior.bookingDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.project.senior.R
import com.project.senior.databinding.FragmentBookingDetailsBinding


class BookingDetailsFragment : Fragment() {

    private lateinit var binding: FragmentBookingDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
                FragmentBookingDetailsBinding.inflate(inflater, container, false)
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
    }

    private fun backToBookingsFragment() {
        findNavController().popBackStack()
    }
}