package com.project.senior.informationDetails

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
import com.project.senior.databinding.FragmentInformationDetailsBinding
import com.project.senior.informationDetails.recyclerview.DetailsAdapter
import com.project.senior.seniorInformation.SeniorInformationViewModel
import com.project.senior.seniorInformation.recyclerview.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentInformationDetailsBinding
    private val viewModel: InformationDetailsViewModel by viewModels()
    private lateinit var detailsAdapter: DetailsAdapter
    private var categoryTitle: String? = null
    private var categoryId: Int? = null
    private var isListEmpty = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding =
            FragmentInformationDetailsBinding.inflate(inflater, container, false)
        val categoryDetailsArgs by navArgs<InformationDetailsFragmentArgs>()
        categoryTitle = categoryDetailsArgs.categoryTitle
        categoryId = categoryDetailsArgs.categoryId
        binding.tvCategoryTitleInformationDetails.text = categoryTitle

        val layoutManger = LinearLayoutManager(context)
        binding.rvInformationDetails.layoutManager = layoutManger
        detailsAdapter = DetailsAdapter()
        binding.rvInformationDetails.adapter = detailsAdapter
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
                binding.rvInformationDetails.visibility = View.GONE
                binding.progressBarInformationDetails.visibility = View.GONE
                binding.groupEmptyDetails.visibility = View.VISIBLE
            }
            else {
                isListEmpty = false
                detailsAdapter.submitList(list)
            }
        })

        clickListener()
    }

    private fun clickListener() {
        binding.imgBackInformationDetails.setOnClickListener {
            backToCategoriesFragment()
        }
    }

    private fun backToCategoriesFragment() {
        findNavController().popBackStack()
    }

    private fun successState() {
        if(isListEmpty) {
            binding.rvInformationDetails.visibility = View.GONE
            binding.progressBarInformationDetails.visibility = View.GONE
            binding.groupEmptyDetails.visibility = View.VISIBLE
        }
        else{
            binding.rvInformationDetails.visibility = View.VISIBLE
            binding.progressBarInformationDetails.visibility = View.GONE
            binding.groupEmptyDetails.visibility = View.GONE
        }
    }

    private fun loadingState() {
        binding.rvInformationDetails.visibility = View.GONE
        binding.groupEmptyDetails.visibility = View.GONE
        binding.progressBarInformationDetails.visibility = View.VISIBLE
    }

    private fun errorState() {
        lifecycleScope.launchWhenCreated {
            viewModel.getCategoryDetails(categoryId!!)
        }
    }
}