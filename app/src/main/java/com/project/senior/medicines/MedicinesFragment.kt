package com.project.senior.medicines

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
import kotlinx.coroutines.runBlocking

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

        medicinesAdapter.onDeleteClick = {medicineData ->
            showDeleteDialog(medicineData.id)
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


    private fun showDeleteDialog(medicineId: Int) {
        fun deleteErrorState() {
            lifecycleScope.launchWhenCreated {
                viewModel.deleteMedicineCategory(medicineId)
            }

        }
        val builder = AlertDialog.Builder(requireContext())
        val title = SpannableString(getString(R.string.delete))
        title.setSpan(ForegroundColorSpan(Color.RED), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setTitle(title)
        builder.setMessage(getString(R.string.medicine_alert_dialog))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            dialog.dismiss()
            lifecycleScope.launchWhenCreated {
                viewModel.deleteMedicineCategory(medicineId)
            }
            viewModel.deleteMedicineResponseState.observe(viewLifecycleOwner, Observer { state->

                when(state){
                    is Resource.Success -> getMedicinesAndSuccessState()
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

    private fun getMedicinesAndSuccessState() {
        runBlocking {
            viewModel.getMedicines(userId!!)
        }
        successState()
    }

    private fun backToBookingDetailsFragment() {
        findNavController().popBackStack()
    }
}