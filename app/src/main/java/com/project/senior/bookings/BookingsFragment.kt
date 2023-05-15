package com.project.senior.bookings

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.bookings.recyclerview.BookingsAdapter
import com.project.senior.databinding.FragmentBookingsBinding
import com.project.senior.seniorInformation.SeniorInformationViewModel
import com.project.senior.seniorInformation.recyclerview.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class BookingsFragment : Fragment() {

    private lateinit var binding: FragmentBookingsBinding
    private val viewModel: BookingsViewModel by viewModels()
    private lateinit var bookingsAdapter: BookingsAdapter
    private var isListEmpty = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentBookingsBinding.inflate(inflater, container, false)
        val layoutManger = LinearLayoutManager(context)
        binding.rvBookings.layoutManager = layoutManger
        bookingsAdapter = BookingsAdapter()
        binding.rvBookings.adapter = bookingsAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.getBookingsData()
        }

        viewModel.getBookingsDataResponseState.observe(viewLifecycleOwner, Observer { state->

            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        viewModel.bookingsList.observe(viewLifecycleOwner, Observer { list ->
            if(list?.size == 0) {
                isListEmpty = true
                binding.rvBookings.visibility = View.GONE
                binding.progressBarBookings.visibility = View.GONE
                binding.groupEmptyBookings.visibility = View.VISIBLE
            }
            else {
                isListEmpty = false
                bookingsAdapter.submitList(list)
            }
        })

        clickListener()
    }

    private fun clickListener() {
        binding.imgBackBookings.setOnClickListener {
            findNavController().popBackStack()
        }

        bookingsAdapter.onCancelClick = {booking ->
            showDeleteDialog(booking.id)
        }
    }

    private fun successState() {
        if (isListEmpty){
            binding.rvBookings.visibility = View.GONE
            binding.progressBarBookings.visibility = View.GONE
            binding.groupEmptyBookings.visibility = View.VISIBLE
        }
        else {
            binding.rvBookings.visibility = View.VISIBLE
            binding.progressBarBookings.visibility = View.GONE
            binding.groupEmptyBookings.visibility = View.GONE
        }
    }

    private fun loadingState() {
        binding.rvBookings.visibility = View.GONE
        binding.groupEmptyBookings.visibility = View.GONE
        binding.progressBarBookings.visibility = View.VISIBLE
    }

    private fun errorState() {
        lifecycleScope.launchWhenCreated {
            viewModel.getBookingsData()
        }
    }

    private fun showDeleteDialog(bookingId: Int) {
        fun deleteErrorState() {
            lifecycleScope.launchWhenCreated {
                viewModel.cancelBooking(bookingId)
            }

        }
        val builder = AlertDialog.Builder(requireContext())
        val title = SpannableString(getString(R.string.delete))
        title.setSpan(ForegroundColorSpan(Color.RED), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setTitle(title)
        builder.setMessage(getString(R.string.bookings_alert_dialog))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            dialog.dismiss()
            lifecycleScope.launchWhenCreated {
                viewModel.cancelBooking(bookingId)
            }
            viewModel.cancelBookingResponseState.observe(viewLifecycleOwner, Observer { state->

                when(state){
                    is Resource.Success -> getBookingsAndSuccessState()
                    is Resource.Loading -> loadingState()
                    is Resource.Error -> deleteErrorState()
                    else -> deleteErrorState()
                }
            })
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()


    }

    private fun getBookingsAndSuccessState() {
        runBlocking {
            viewModel.getBookingsData()
        }
        successState()
    }
}