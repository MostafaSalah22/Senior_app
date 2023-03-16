package com.project.senior.schedule

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
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentScheduleBinding
import com.project.senior.schedule.recyclerview.ScheduleAdapter
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
        val layoutManger = LinearLayoutManager(context)
        binding.rvSchedule.layoutManager = layoutManger
        scheduleAdapter = ScheduleAdapter()
        binding.rvSchedule.adapter = scheduleAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.getSchedules(userId!!)
        }

        viewModel.getSchedulesResponseState.observe(viewLifecycleOwner, Observer { state->

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

    private fun clickListener() {
        binding.imgNextdaySchedule.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModel.nextDayClick()
            }
        }

        binding.imgPerviousdaySchedule.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModel.previousDayClick()
            }
        }

        binding.tvDateSchedule.setOnClickListener {
            viewModel.showCalender(requireActivity())
        }

        binding.imgBackSchedule.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgAddSchedule.setOnClickListener {
            showNewEventDialog(requireContext())
        }

        scheduleAdapter.onCancelClick = { schedule->
            showCancelDialog(schedule.id)
        }
    }

    private fun showCancelDialog(scheduleId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val title = SpannableString("Delete")
        title.setSpan(ForegroundColorSpan(Color.RED), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setTitle(title)
        builder.setMessage("Are you sure you want to delete this appointment?")
        builder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            lifecycleScope.launchWhenCreated {
                viewModel.cancelSchedule(scheduleId)
            }
            viewModel.cancelScheduleResponseState.observe(viewLifecycleOwner, Observer { state->

                when(state){
                    is Resource.Success -> cancelSuccessState()
                    is Resource.Loading -> loadingState()
                    is Resource.Error -> errorState()
                    else -> errorState()
                }
            })
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private var isListEmpty = false
    private fun successState() {
        viewModel.scheduleList.observe(viewLifecycleOwner, Observer { scheduleList->
            if (scheduleList.size == 0) {
                isListEmpty = true
                binding.rvSchedule.visibility = View.GONE
                binding.progressBarSchedule.visibility =View.GONE
                binding.groupEmptySchedule.visibility = View.VISIBLE
            }
            else {
                isListEmpty = false
                binding.rvSchedule.visibility = View.VISIBLE
                binding.progressBarSchedule.visibility =View.GONE
                binding.groupEmptySchedule.visibility = View.GONE
            }
        })
    }

    private fun cancelSuccessState() {
        runBlocking {
            viewModel.getSchedules(userId!!)
        }
        successState()
    }

    private fun loadingState() {
        binding.rvSchedule.visibility = View.GONE
        binding.groupEmptySchedule.visibility = View.GONE
        binding.progressBarSchedule.visibility =View.VISIBLE
    }

    private fun errorState() {
        Snackbar.make(requireView(), "Something Error! Please, Try Again.",Snackbar.LENGTH_LONG).show()
        if(isListEmpty) {
            binding.rvSchedule.visibility = View.GONE
            binding.progressBarSchedule.visibility = View.GONE
            binding.groupEmptySchedule.visibility = View.VISIBLE
        }

        else {
            binding.rvSchedule.visibility = View.VISIBLE
            binding.progressBarSchedule.visibility = View.GONE
            binding.groupEmptySchedule.visibility = View.GONE
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
        myDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val title = dialogBinding.findViewById<EditText>(R.id.et_title_new_event)
        val date = dialogBinding.findViewById<EditText>(R.id.et_date_new_event)
        val time = dialogBinding.findViewById<EditText>(R.id.et_time_new_event)
        val description = dialogBinding.findViewById<EditText>(R.id.et_description_new_event)

        val addNewEventBtn = dialogBinding.findViewById<Button>(R.id.btn_add_new_event)
        date.setOnClickListener {
            viewModel.showCalenderEt(requireActivity())
        }

        time.setOnClickListener {
            viewModel.showTimeDialog(requireActivity())
        }

        viewModel.time.observe(viewLifecycleOwner, Observer { setTime->
            time.setText(setTime)
        })
        viewModel.dateEt.observe(viewLifecycleOwner, Observer { calenderDate->
            date.setText(calenderDate)
        })
        addNewEventBtn.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModel.addNewSchedule(userId!!, title.text.toString(), date.text.toString(), time.text.toString(), description.text.toString())
            }
            viewModel.addNewScheduleResponseState.observe(viewLifecycleOwner, Observer { state->
                when(state){
                    is Resource.Success -> addSuccessState(myDialog)
                    is Resource.Loading -> addLoadingState(myDialog)
                    is Resource.Error -> errorState()
                    else -> errorState()
                }
            })
        }

        val backNewEvent = dialogBinding.findViewById<ImageView>(R.id.img_back_new_event)
        backNewEvent.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun addSuccessState(myDialog: Dialog) {
        lifecycleScope.launchWhenCreated {
            viewModel.getSchedules(userId!!)
        }
        myDialog.dismiss()
        successState()
    }

    private fun addLoadingState(myDialog: Dialog) {
        myDialog.dismiss()
        loadingState()
    }

}