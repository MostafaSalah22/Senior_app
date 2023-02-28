package com.project.senior.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.senior.databinding.FragmentNotificationBinding
import com.project.senior.notification.recyclerview.NotificationAdapter
import com.project.senior.notification.recyclerview.NotificationModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding:FragmentNotificationBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private val arr:ArrayList<NotificationModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentNotificationBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arr.add(NotificationModel(0,"title1","you must take bla bla"))
        arr.add(NotificationModel(1,"title2","you must take bla bla"))
        arr.add(NotificationModel(2,"title3","you must take bla bla"))
        arr.add(NotificationModel(3,"title4","you must take bla bla"))
        arr.add(NotificationModel(4,"title5","you must take bla bla"))
        arr.add(NotificationModel(5,"title6","you must take bla bla"))
        arr.add(NotificationModel(6,"title7","you must take bla bla"))
        arr.add(NotificationModel(7,"title8","you must take bla bla"))
        arr.add(NotificationModel(8,"title9","you must take bla bla"))
        arr.add(NotificationModel(9,"title10","you must take bla bla"))
        arr.add(NotificationModel(10,"title11","you must take bla bla"))

        val layoutManger = LinearLayoutManager(context)
        binding.rvNotification.layoutManager = layoutManger
        notificationAdapter = NotificationAdapter()
        binding.rvNotification.adapter = notificationAdapter
        notificationAdapter.submitList(arr)

        clickListener()

    }

    private fun clickListener() {
        binding.includeNotification.iconNotification.setOnClickListener {
            backToChatFragment()
        }

        binding.includeNotification.imgProfileNotification.setOnClickListener {
            navigateToProfileFragment()
        }

        binding.includeNotification.tvContactsNotification.setOnClickListener {
            backToChatFragment()
        }
    }

    private fun backToChatFragment() {
        findNavController().popBackStack()
    }

    private fun navigateToProfileFragment() {
        findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToProfileFragment())
    }

}