package com.project.senior.schedule

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentScheduleBinding
import com.project.senior.login.LoginViewModel
import com.project.senior.schedule.recyclerview.ScheduleAdapter
import com.project.senior.schedule.recyclerview.ScheduleModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var scheduleAdapter: ScheduleAdapter
    //private val arr:ArrayList<ScheduleModel> = ArrayList()
    private var userId: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentScheduleBinding.inflate(inflater , container , false)
        val scheduleFragmentArgs by navArgs<ScheduleFragmentArgs>()
        userId = scheduleFragmentArgs.userId
        //scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*arr.add(ScheduleModel(0,"Doctor","5:30pm","go to doctor go to doctor go to doctor go to doctor"))
        arr.add(ScheduleModel(1,"Doctor","5:30pm","go to doctor go to doctor go to doctor go to doctor"))
        arr.add(ScheduleModel(2,"Doctor","5:30pm","go to doctor go to doctor go to doctor go to doctor"))
        arr.add(ScheduleModel(3,"Doctor","5:30pm","go to doctor go to doctor go to doctor go to doctor"))
        arr.add(ScheduleModel(4,"Doctor","5:30pm","go to doctor go to doctor go to doctor go to doctor"))
        arr.add(ScheduleModel(5,"Doctor","5:30pm","go to doctor go to doctor go to doctor go to doctor"))
        arr.add(ScheduleModel(6,"Doctor","5:30pm","go to doctor go to doctor go to doctor go to doctor"))
        arr.add(ScheduleModel(7,"Doctor","5:30pm","go to doctor go to doctor go to doctor go to doctor"))
        arr.add(ScheduleModel(8,"Doctor00","5:30pm","go to doctor go to doctor go to doctor go to doctor"))*/

        val layoutManger = LinearLayoutManager(context)
        binding.rvSchedule.layoutManager = layoutManger
        scheduleAdapter = ScheduleAdapter()
        binding.rvSchedule.adapter = scheduleAdapter
        //scheduleAdapter.submitList(arr)
        lifecycleScope.launchWhenCreated {
            viewModel.getSchedules(userId!!)
        }

        viewModel.getSchedulesResponseState.observe(viewLifecycleOwner, Observer { state->

            Log.i("ScheduleViewModel", "onViewCreated: $state")

            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        viewModel.scheduleList.observe(viewLifecycleOwner, Observer { scheduleList->

            scheduleAdapter.submitList(scheduleList)

        })

        viewModel.date.observe(viewLifecycleOwner, Observer { date ->
            binding.tvDateSchedule.text = date
        })

        viewModel.dayName.observe(viewLifecycleOwner, Observer { day ->
            binding.tvDaySchedule.text = day
        })

        clickListener()
    }

    private fun successState() {
        binding.rvSchedule.visibility = View.VISIBLE
        binding.progressBarSchedule.visibility =View.GONE
    }

    private fun loadingState() {
        binding.rvSchedule.visibility = View.GONE
        binding.progressBarSchedule.visibility =View.VISIBLE
    }

    private fun errorState() {
        Snackbar.make(requireView(), "Something Error! Please, Try Again.",Snackbar.LENGTH_LONG).show()
    }

    private fun clickListener() {
        binding.imgNextdaySchedule.setOnClickListener {
            runBlocking {
                viewModel.nextDayClick(userId!!)
            }
        }

        binding.imgPerviousdaySchedule.setOnClickListener {
            runBlocking {
                viewModel.previousDayClick(userId!!)
            }
        }

        binding.tvDateSchedule.setOnClickListener {
            viewModel.showCalender(requireActivity(), userId!!)
        }

        binding.imgBackSchedule.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgAddSchedule.setOnClickListener {
            showNewEventDialog(requireContext())
        }
    }

    private fun showNewEventDialog(context: Context) {
        val dialogBinding = layoutInflater.inflate(R.layout.new_event_dialog,null)

        val myDialog = Dialog(context)
        myDialog.setContentView(dialogBinding)

        myDialog.setCancelable(true)

        // Make the dialog width (match_parent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(myDialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT

        myDialog.show()
        myDialog.window?.attributes = lp

        val addNewEventBtn = dialogBinding.findViewById<Button>(R.id.btn_add_new_event)
        addNewEventBtn.setOnClickListener {
            myDialog.dismiss()
        }

        val backNewEvent = dialogBinding.findViewById<ImageView>(R.id.img_back_new_event)
        backNewEvent.setOnClickListener {
            myDialog.dismiss()
        }
    }

}