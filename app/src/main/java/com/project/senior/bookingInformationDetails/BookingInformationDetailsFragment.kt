package com.project.senior.bookingInformationDetails

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
import com.project.senior.bookingInformationDetails.recyclerview.BookingInformationDetailsAdapter
import com.project.senior.databinding.FragmentBookingInformationDetailsBinding
import com.project.senior.informationDetails.InformationDetailsFragmentArgs
import com.project.senior.informationDetails.InformationDetailsViewModel
import com.project.senior.informationDetails.recyclerview.DetailsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingInformationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentBookingInformationDetailsBinding
    private val viewModel: BookingInformationDetailsViewModel by viewModels()
    private var categoryTitle: String? = null
    private var categoryId: Int? = null
    private var isListEmpty = false
    private lateinit var bookingInformationDetailsAdapter: BookingInformationDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
                FragmentBookingInformationDetailsBinding.inflate(inflater, container, false)
        val bookingInformationDetailsArgs by navArgs<BookingInformationDetailsFragmentArgs>()
        categoryTitle = bookingInformationDetailsArgs.categoryName
        categoryId = bookingInformationDetailsArgs.categoryId

        binding.tvCategoryTitleBookingInformationDetails.text = categoryTitle

        val layoutManger = LinearLayoutManager(context)
        binding.rvBookingInformationDetails.layoutManager = layoutManger
        bookingInformationDetailsAdapter = BookingInformationDetailsAdapter()
        binding.rvBookingInformationDetails.adapter = bookingInformationDetailsAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.getCategoryDetails(categoryId!!)
        }

        viewModel.getCategoryDetailsResponseState.observe(viewLifecycleOwner, Observer { state->

            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        viewModel.detailsList.observe(viewLifecycleOwner, Observer { list ->
            if(list?.size == 0) {
                isListEmpty = true
                binding.rvBookingInformationDetails.visibility = View.GONE
                binding.progressBarBookingInformationDetails.visibility = View.GONE
                binding.groupEmptyBookingInformationDetails.visibility = View.VISIBLE
            }
            else {
                isListEmpty = false
                bookingInformationDetailsAdapter.submitList(list)
            }
        })

        clickListener()
    }

    private fun successState() {
        if(isListEmpty) {
            binding.rvBookingInformationDetails.visibility = View.GONE
            binding.progressBarBookingInformationDetails.visibility = View.GONE
            binding.groupEmptyBookingInformationDetails.visibility = View.VISIBLE
        }
        else{
            binding.rvBookingInformationDetails.visibility = View.VISIBLE
            binding.progressBarBookingInformationDetails.visibility = View.GONE
            binding.groupEmptyBookingInformationDetails.visibility = View.GONE
        }
    }

    private fun loadingState() {
        binding.rvBookingInformationDetails.visibility = View.GONE
        binding.groupEmptyBookingInformationDetails.visibility = View.GONE
        binding.progressBarBookingInformationDetails.visibility = View.VISIBLE
    }

    private fun errorState() {
        lifecycleScope.launchWhenCreated {
            viewModel.getCategoryDetails(categoryId!!)
        }
    }

    private fun clickListener(){
        binding.imgBackBookingInformationDetails.setOnClickListener {
            backToBookingInformationFragment()
        }
    }

    private fun backToBookingInformationFragment(){
        findNavController().popBackStack()
    }
}