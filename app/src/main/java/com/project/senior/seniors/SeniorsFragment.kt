package com.project.senior.seniors

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentSeniorsBinding
import com.project.senior.seniors.recyclerview.SeniorsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class SeniorsFragment : Fragment() {

    private lateinit var binding: FragmentSeniorsBinding
    private lateinit var seniorsAdapter: SeniorsAdapter
    private val viewModel: SeniorsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSeniorsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.getMySeniors()
        }

        viewModel.getSeniorsResponseState.observe(viewLifecycleOwner, Observer { state ->

            when (state) {
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState("Something Error! please, Try Again.")
                else -> errorState("Something Error! please, Try Again.")
            }

        })

        val layoutManger = LinearLayoutManager(context)
        binding.rvSeniors.layoutManager = layoutManger
        seniorsAdapter = SeniorsAdapter()
        binding.rvSeniors.adapter = seniorsAdapter

        clickListener()
    }

    private fun clickListener() {
        binding.imgBackSeniors.setOnClickListener {
            backToProfileFragment()
        }

        binding.fabAddSeniors.setOnClickListener {
            showNewSeniorDialog(requireContext())
        }

        seniorsAdapter.onProfileClick = {seniorData->
            navigateToSeniorDetailsFragment(seniorData.id)
        }

        seniorsAdapter.onDeleteClick = {seniorData->
            showDeleteDialog(seniorData.id)
        }
    }

    private fun showDeleteDialog(seniorId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val title = SpannableString("Delete")
        title.setSpan(ForegroundColorSpan(Color.RED), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setTitle(title)
        builder.setMessage("Are you sure you want to delete this senior?")
        builder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            lifecycleScope.launchWhenCreated {
                lifecycleScope.launchWhenCreated {
                    viewModel.deleteSenior(seniorId)
                }

                viewModel.deleteSeniorResponseState.observe(viewLifecycleOwner, Observer { state->
                    when (state) {
                        is Resource.Success -> deleteSeniorSuccessState()
                        is Resource.Loading -> loadingState()
                        is Resource.Error -> errorState("Something Error! please, check username and try again.")
                        else -> errorState("Something Error! please, check username and try again.")
                    }
                })
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private var isListEmpty = false
    private fun successState() {
        viewModel.seniorsList.observe(viewLifecycleOwner, Observer {seniorsList ->
            if (seniorsList.size == 0) {
                isListEmpty = true
                binding.rvSeniors.visibility = View.GONE
                binding.groupEmptySeniors.visibility = View.VISIBLE
                binding.progressBarSeniors.visibility = View.GONE
                binding.fabAddSeniors.visibility = View.VISIBLE
            }
            else {
                isListEmpty = false
                seniorsAdapter.submitList(seniorsList)
                binding.groupSeniors.visibility = View.VISIBLE
                binding.progressBarSeniors.visibility = View.GONE
            }
        })
    }

    private fun deleteSeniorSuccessState() {
        runBlocking {
            viewModel.getMySeniors()
        }
        viewModel.deleteSeniorMessage.observe(viewLifecycleOwner, Observer { message->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        })
        successState()
    }

    private fun loadingState() {
        binding.groupSeniors.visibility = View.GONE
        binding.groupEmptySeniors.visibility = View.GONE
        binding.progressBarSeniors.visibility = View.VISIBLE
    }

    private fun errorState(message: String) {
        Snackbar.make(requireView(),message, Snackbar.LENGTH_LONG).show()
        if(isListEmpty) {
            binding.rvSeniors.visibility = View.GONE
            binding.progressBarSeniors.visibility = View.GONE
            binding.groupEmptySeniors.visibility = View.VISIBLE
            binding.fabAddSeniors.visibility = View.VISIBLE
        }
        else {
            binding.groupSeniors.visibility = View.VISIBLE
            binding.progressBarSeniors.visibility = View.GONE
            binding.groupEmptySeniors.visibility = View.GONE
        }
    }

    private fun backToProfileFragment() {
        findNavController().popBackStack()
    }

    private fun navigateToSeniorDetailsFragment(userId: Int) {
        findNavController().navigate(SeniorsFragmentDirections.actionSeniorsFragmentToSeniorDetailsFragment(userId))
    }

    private fun showNewSeniorDialog(context: Context) {
        val dialogBinding = layoutInflater.inflate(R.layout.new_senior_dialog,null)

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

        val username = dialogBinding.findViewById<EditText>(R.id.et_username_new_senior)

        val addNewEventBtn = dialogBinding.findViewById<Button>(R.id.btn_submit_new_senior)
        addNewEventBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.addNewSenior(username.text.toString())
            }
            viewModel.addSeniorResponseState.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    is Resource.Success -> addSeniorSuccessState(myDialog)
                    is Resource.Loading -> addSeniorLoadingState(myDialog)
                    is Resource.Error -> errorState("Something Error! please, check username and try again.")
                    else -> errorState("Something Error! please, check username and try again.")
                }

            })
        }

        val backNewEvent = dialogBinding.findViewById<ImageView>(R.id.img_back_new_senior)
        backNewEvent.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun addSeniorSuccessState(myDialog: Dialog) {
        runBlocking {
            viewModel.getMySeniors()
        }
        viewModel.addSeniorMessage.observe(viewLifecycleOwner, Observer { message->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        })
        myDialog.dismiss()
        successState()
    }
    private fun addSeniorLoadingState(myDialog: Dialog) {
        myDialog.dismiss()
        loadingState()
    }

}