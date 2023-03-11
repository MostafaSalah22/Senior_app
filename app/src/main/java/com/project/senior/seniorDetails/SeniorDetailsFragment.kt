package com.project.senior.seniorDetails

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentSeniorDetailsBinding
import com.project.senior.databinding.FragmentSeniorsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeniorDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSeniorDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSeniorDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListener()
    }

    private fun clickListener() {
        binding.cardScheduleSeniorDetails.setOnClickListener {
            val detailsFragmentArgs by navArgs<SeniorDetailsFragmentArgs>()
            val userId = detailsFragmentArgs.userId
            navigateToScheduleFragment(userId)
        }

        binding.cardNotificationSeniorDetails.setOnClickListener {
            sendNotificationDialog(requireContext())
        }

        binding.imgBackSeniorDetails.setOnClickListener {
            backToSeniorsFragment()
        }
    }

    private fun navigateToScheduleFragment(userId: Int) {
        findNavController().navigate(SeniorDetailsFragmentDirections.actionSeniorDetailsFragmentToScheduleFragment(userId))
    }

    private fun backToSeniorsFragment() {
        findNavController().popBackStack()
    }

    private fun sendNotificationDialog(context: Context) {
        val dialogBinding = layoutInflater.inflate(R.layout.send_notificaction_dialog,null)

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

        val title = dialogBinding.findViewById<EditText>(R.id.et_title_notification)
        val content = dialogBinding.findViewById<EditText>(R.id.et_content_notification)

        val addNewEventBtn = dialogBinding.findViewById<Button>(R.id.btn_send_notification)
        addNewEventBtn.setOnClickListener {
            myDialog.dismiss()
        }

        val backNewEvent = dialogBinding.findViewById<ImageView>(R.id.img_back_notification)
        backNewEvent.setOnClickListener {
            myDialog.dismiss()
        }
    }

}