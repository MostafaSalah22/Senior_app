package com.project.senior.seniorInformation

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
import com.project.senior.databinding.FragmentSeniorDetailsBinding
import com.project.senior.databinding.FragmentSeniorInformationBinding
import com.project.senior.schedule.ScheduleViewModel
import com.project.senior.schedule.recyclerview.ScheduleAdapter
import com.project.senior.seniorDetails.SeniorDetailsFragmentArgs
import com.project.senior.seniorInformation.recyclerview.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeniorInformationFragment : Fragment() {

    private lateinit var binding: FragmentSeniorInformationBinding
    private val viewModel: SeniorInformationViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSeniorInformationBinding.inflate(inflater,container,false)
        val informationFragmentArgs by navArgs<SeniorInformationFragmentArgs>()
        userId = informationFragmentArgs.userId
        val layoutManger = LinearLayoutManager(context)
        binding.rvInformation.layoutManager = layoutManger
        categoriesAdapter = CategoriesAdapter()
        binding.rvInformation.adapter = categoriesAdapter
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

        categoriesAdapter.submitList(list)

        })
        clickListener()
    }

    private fun successState() {
        binding.rvInformation.visibility = View.VISIBLE
        binding.progressBarInformation.visibility = View.GONE
    }

    private fun loadingState() {
        binding.rvInformation.visibility = View.GONE
        binding.progressBarInformation.visibility = View.VISIBLE
    }

    private fun errorState() {

    }

    private fun clickListener() {
        binding.imgBackSeniorInformation.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}