package com.project.senior.bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.senior.R
import com.project.senior.databinding.FragmentBookingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsFragment : Fragment() {

    private lateinit var binding: FragmentBookingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }


}