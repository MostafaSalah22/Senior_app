package com.project.senior.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.senior.R
import com.project.senior.chat.recyclerview.ChatAdapter
import com.project.senior.chat.recyclerview.ChatModel
import com.project.senior.databinding.FragmentChatBinding
import com.project.senior.notification.recyclerview.NotificationAdapter
import com.project.senior.notification.recyclerview.NotificationModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val arr:ArrayList<ChatModel> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentChatBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arr.add(ChatModel(0,"Abdullah","hello, how are you?"))
        arr.add(ChatModel(1,"Ahmed","hello, how are you?"))
        arr.add(ChatModel(2,"khairy","hello, how are you?"))
        arr.add(ChatModel(3,"Mostafa","hello, how are you?"))
        arr.add(ChatModel(4,"Mohamed","hello, how are you?"))
        arr.add(ChatModel(5,"Adb Elrahman","hello, how are you?"))
        arr.add(ChatModel(6,"Ali","hello, how are you?"))
        arr.add(ChatModel(7,"Salah","hello, how are you?"))
        arr.add(ChatModel(8,"Mahmoud","hello, how are you?"))
        arr.add(ChatModel(9,"Yasser","hello, how are you?"))

        val layoutManger = LinearLayoutManager(context)
        binding.rvChat.layoutManager = layoutManger
        chatAdapter = ChatAdapter()
        binding.rvChat.adapter = chatAdapter
        chatAdapter.submitList(arr)

        clickListener()

    }

    private fun clickListener() {
        binding.includeChat.imgProfileChat.setOnClickListener {
            navigateToProfileFragment()
        }

        binding.includeChat.iconNotificationChat.setOnClickListener {
            navigateToNotificationFragment()
        }
    }

    private fun navigateToProfileFragment() {
        findNavController().navigate(ChatFragmentDirections.actionChatFragmentToProfileFragment())
    }

    private fun navigateToNotificationFragment() {
        findNavController().navigate(ChatFragmentDirections.actionChatFragmentToNotificationFragment())
    }
}