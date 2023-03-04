package com.project.senior.seniors

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.chat.recyclerview.ChatAdapter
import com.project.senior.chat.recyclerview.ChatModel
import com.project.senior.databinding.FragmentSeniorsBinding
import com.project.senior.profile.ProfileViewModel
import com.project.senior.seniors.recyclerview.SeniorModel
import com.project.senior.seniors.recyclerview.SeniorsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeniorsFragment : Fragment() {

    private lateinit var binding: FragmentSeniorsBinding
    private lateinit var seniorsAdapter: SeniorsAdapter
    private val viewModel: SeniorsViewModel by viewModels()
    private val arr:ArrayList<SeniorModel> = ArrayList()

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
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })
        /*arr.add(SeniorModel(0,"Mostafa Salah",65))
        arr.add(SeniorModel(1,"Mostafa Salah",65))
        arr.add(SeniorModel(2,"Mostafa Salah",65))
        arr.add(SeniorModel(3,"Mostafa Salah",65))
        arr.add(SeniorModel(4,"Mostafa Salah",65))
        arr.add(SeniorModel(5,"Mostafa Salah",65))
        arr.add(SeniorModel(6,"Mostafa Salah",65))
        arr.add(SeniorModel(7,"Mostafa Salah",65))*/

        val layoutManger = LinearLayoutManager(context)
        binding.rvSeniors.layoutManager = layoutManger
        seniorsAdapter = SeniorsAdapter()
        binding.rvSeniors.adapter = seniorsAdapter
        //seniorsAdapter.submitList(arr)

        clickListener()
    }

    private fun clickListener() {
        binding.imgBackSeniors.setOnClickListener {
            backToProfileFragment()
        }

        binding.fabAddSeniors.setOnClickListener {
            showNewSeniorDialog(requireContext())
        }

        seniorsAdapter.onItemClick = {
            navigateToScheduleFragment()
        }
    }

    private fun successState() {
        viewModel.seniorsList.observe(viewLifecycleOwner, Observer {seniorsList ->
        seniorsAdapter.submitList(seniorsList)
        })

        binding.groupSeniors.visibility = View.VISIBLE
        binding.progressBarSeniors.visibility = View.GONE
    }

    private fun loadingState() {
        binding.groupSeniors.visibility = View.GONE
        binding.progressBarSeniors.visibility = View.VISIBLE
    }

    private fun errorState() {
        Snackbar.make(requireView(),"Something Error! please, Try Again.", Snackbar.LENGTH_LONG).show()
        binding.groupSeniors.visibility = View.VISIBLE
        binding.progressBarSeniors.visibility = View.GONE
    }

    private fun backToProfileFragment() {
        findNavController().popBackStack()
    }

    private fun navigateToScheduleFragment() {
        findNavController().navigate(SeniorsFragmentDirections.actionSeniorsFragmentToScheduleFragment())
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

        val addNewEventBtn = dialogBinding.findViewById<Button>(R.id.btn_submit_new_senior)
        addNewEventBtn.setOnClickListener {
            myDialog.dismiss()
        }

        val backNewEvent = dialogBinding.findViewById<ImageView>(R.id.img_back_new_senior)
        backNewEvent.setOnClickListener {
            myDialog.dismiss()
        }
    }

}