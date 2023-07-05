package com.project.senior.medicines

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentMedicinesBinding
import com.project.senior.informationDetails.InformationDetailsFragmentDirections
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

        medicinesAdapter.onEditClick = {medicineData ->
            medicineDialog(requireContext(), medicineData.id, 1, medicineData.medication, medicineData.description, medicineData.medication_dose)
        }

        binding.fabAddMedicine.setOnClickListener {
            medicineDialog(requireContext(), userId!!, 2, null, null, null)
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

    private fun medicineDialog(context: Context, requiredId: Int, funId: Int, medicineName: String?, medicineDescription: String?, dose: Int?) {
        val dialogBinding = layoutInflater.inflate(R.layout.add_medicine_dialog,null)

        val myDialog = Dialog(context)
        myDialog.setContentView(dialogBinding)

        myDialog.setCancelable(true)

        // Make the dialog width (match_parent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(myDialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT

        myDialog.show()
        myDialog.window?.attributes = lp
        myDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val name = dialogBinding.findViewById<EditText>(R.id.et_name_medicine_dialog)
        val description = dialogBinding.findViewById<EditText>(R.id.et_description_medicine_dialog)
        val doseEt = dialogBinding.findViewById<EditText>(R.id.et_dose_medicine_dialog)

        if (medicineName != null){
            name.setText(medicineName)
            description.setText(medicineDescription)
            doseEt.setText(dose.toString())
        }

        val saveBtn = dialogBinding.findViewById<Button>(R.id.btn_save_medicine_dialog)
        saveBtn.setOnClickListener {
            if(name.text.toString().trim() == "" || description.text.toString().trim() == "" || doseEt.text.toString().trim() == "") {
                name.error = getString(R.string.enter_all_data)
                name.requestFocus()
                description.error = getString(R.string.enter_all_data)
                description.requestFocus()
                doseEt.error = getString(R.string.enter_all_data)
                doseEt.requestFocus()
            }
            else {
                if(funId == 1) {
                    lifecycleScope.launchWhenCreated {
                        viewModel.updateMedicine(
                            requiredId,
                            name.text.toString().trim(),
                            doseEt.text.toString().trim().toInt(),
                            description.text.toString().trim()
                        )
                    }
                    viewModel.updateMedicineResponseState.observe(
                        viewLifecycleOwner,
                        Observer { state ->
                            when (state) {
                                is Resource.Success -> editSuccessState()
                                is Resource.Loading -> dismissDialogAndLoadingState(myDialog)
                                is Resource.Error -> editErrorState(
                                    requiredId,
                                    name.text.toString().trim(),
                                    description.text.toString().trim(),
                                    doseEt.text.toString().trim().toInt()
                                )
                                else -> editErrorState(requiredId, name.text.toString().trim(), description.text.toString().trim(), doseEt.text.toString().trim().toInt())
                            }
                        })
                }
                else{
                    lifecycleScope.launchWhenCreated {
                        viewModel.addNewMedicine(userId!!,
                                                    name.text.toString().trim(),
                                                    doseEt.text.toString().trim().toInt(),
                                                    description.text.toString().trim()
                        )
                    }
                    viewModel.addNewMedicineResponseState.observe(
                        viewLifecycleOwner,
                        Observer { state ->
                            when (state) {
                                is Resource.Success -> getMedicinesAndSuccessState()
                                is Resource.Loading -> dismissDialogAndLoadingState(myDialog)
                                is Resource.Error -> addErrorState(
                                    requiredId,
                                    name.text.toString().trim(),
                                    description.text.toString().trim(),
                                    doseEt.text.toString().trim().toInt()
                                )
                                else -> addErrorState(requiredId, name.text.toString().trim(), description.text.toString().trim(), doseEt.text.toString().trim().toInt())
                            }
                        })
                }
            }
        }

        val backEditDetails = dialogBinding.findViewById<ImageView>(R.id.img_back_medicine_dialog)
        backEditDetails.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun editSuccessState() {
        runBlocking {
            viewModel.getMedicines(userId!!)
        }
        findNavController().navigate(MedicinesFragmentDirections.actionMedicinesFragmentSelf(userId!!))
        //successState()
    }

    private fun dismissDialogAndLoadingState(myDialog: Dialog){
        myDialog.dismiss()
        loadingState()
    }

    private fun editErrorState(userId: Int, name:String, description: String, dose:Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.updateMedicine(userId, name, dose, description)
        }
    }

    private fun addErrorState(userId: Int, name:String, description: String, dose: Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.addNewMedicine(userId, name, dose, description)
        }
    }

    private fun backToBookingDetailsFragment() {
        findNavController().popBackStack()
    }
}