package com.project.senior.bookingInformation

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.bookingInformation.recyclerview.BookingInformationAdapter
import com.project.senior.databinding.FragmentBookingInformationBinding
import com.project.senior.seniorInformation.SeniorInformationFragmentArgs
import com.project.senior.seniorInformation.SeniorInformationViewModel
import com.project.senior.seniorInformation.recyclerview.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingInformationFragment : Fragment() {

    private lateinit var binding: FragmentBookingInformationBinding
    private lateinit var bookingInformationAdapter: BookingInformationAdapter
    private val viewModel: BookingInformationViewModel by viewModels()
    private var userId: Int? = null
    private var isListEmpty = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
                FragmentBookingInformationBinding.inflate(inflater, container, false)

        val bookingInformationFragmentArgs by navArgs<BookingInformationFragmentArgs>()
        userId = bookingInformationFragmentArgs.userId

        val layoutManger = LinearLayoutManager(context)
        binding.rvBookingInformation.layoutManager = layoutManger
        bookingInformationAdapter = BookingInformationAdapter()
        binding.rvBookingInformation.adapter = bookingInformationAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.getInformationCategories(userId!!)
        }

        viewModel.getInformationCategoriesResponseState.observe(viewLifecycleOwner, Observer { state->

            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        viewModel.categoriesList.observe(viewLifecycleOwner, Observer {list ->
            if(list?.size == 0) {
                isListEmpty = true
                binding.rvBookingInformation.visibility = View.GONE
                binding.progressBarBookingInformation.visibility = View.GONE
                binding.groupEmptyInformation.visibility = View.VISIBLE
            }
            else {
                isListEmpty = false
                bookingInformationAdapter.submitList(list)
            }

        })

        clickListener()
    }

    private fun successState() {
        if (isListEmpty){
            binding.rvBookingInformation.visibility = View.GONE
            binding.progressBarBookingInformation.visibility = View.GONE
            binding.groupEmptyInformation.visibility = View.VISIBLE
        }
        else {
            binding.rvBookingInformation.visibility = View.VISIBLE
            binding.progressBarBookingInformation.visibility = View.GONE
            binding.groupEmptyInformation.visibility = View.GONE
        }
    }

    private fun loadingState() {
        binding.rvBookingInformation.visibility = View.GONE
        binding.groupEmptyInformation.visibility = View.GONE
        binding.progressBarBookingInformation.visibility = View.VISIBLE
    }

    private fun errorState() {
        lifecycleScope.launchWhenCreated {
            viewModel.getInformationCategories(userId!!)
        }
    }

    private fun clickListener() {
        binding.imgBackBookingInformation.setOnClickListener {
            backToBookingDetailsFragment()
        }

        bookingInformationAdapter.onItemClick = {categoryData ->

            findNavController().navigate(BookingInformationFragmentDirections.actionBookingInformationFragmentToBookingInformationDetailsFragment(
                categoryData.id,
                categoryData.title
            ))

        }
    }

    private fun backToBookingDetailsFragment(){
        findNavController().popBackStack()
    }

}