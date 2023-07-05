package com.project.senior.medicines

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
import com.project.senior.databinding.FragmentMedicinesBinding
import com.project.senior.informationDetails.InformationDetailsViewModel
import com.project.senior.informationDetails.recyclerview.DetailsAdapter
import com.project.senior.medicines.recyclerview.MedicineAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicinesFragment : Fragment() {

    private lateinit var binding: FragmentMedicinesBinding
    private val viewModel: MedicinesViewModel by viewModels()
    private lateinit var medicinesAdapter: MedicineAdapter
    private var userId: Int? = null
    private var isListEmpty = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMedicinesBinding.inflate(inflater, container, false)

        val medicinesFragmentArgs by navArgs<MedicinesFragmentArgs>()
        userId = medicinesFragmentArgs.userId

        val layoutManger = LinearLayoutManager(context)
        binding.rvMedicines.layoutManager = layoutManger
        medicinesAdapter = MedicineAdapter(requireContext())
        binding.rvMedicines.adapter = medicinesAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.getMedicines(userId!!)
        }

        viewModel.getMedicinesResponseState.observe(viewLifecycleOwner, Observer { state->

            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        viewModel.medicinesList.observe(viewLifecycleOwner, Observer { list ->
            if(list?.size == 0) {
                isListEmpty = true
                binding.rvMedicines.visibility = View.GONE
                binding.progressBarMedicines.visibility = View.GONE
                binding.groupEmptyMedicines.visibility = View.VISIBLE
            }
            else {
                isListEmpty = false
                medicinesAdapter.submitList(list)
            }
        })

        clickListener()
    }

    private fun clickListener() {
        binding.imgBackMedicines.setOnClickListener {
            backToBookingDetailsFragment()
        }
    }

    private fun successState() {
        if(isListEmpty) {
            binding.rvMedicines.visibility = View.GONE
            binding.progressBarMedicines.visibility = View.GONE
            binding.groupEmptyMedicines.visibility = View.VISIBLE
        }
        else{
            binding.rvMedicines.visibility = View.VISIBLE
            binding.progressBarMedicines.visibility = View.GONE
            binding.groupEmptyMedicines.visibility = View.GONE
        }
    }

    private fun loadingState() {
        binding.rvMedicines.visibility = View.GONE
        binding.groupEmptyMedicines.visibility = View.GONE
        binding.progressBarMedicines.visibility = View.VISIBLE
    }

    private fun errorState() {
        lifecycleScope.launchWhenCreated {
            viewModel.getMedicines(userId!!)
        }
    }

    private fun backToBookingDetailsFragment() {
        findNavController().popBackStack()
    }
}