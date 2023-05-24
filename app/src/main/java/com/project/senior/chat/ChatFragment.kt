package com.project.senior.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.project.domain.model.AppUser
import com.project.domain.model.ChatModel
import com.project.senior.chat.recyclerview.ChatAdapter
import com.project.senior.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter
    private val usersList:ArrayList<ChatModel> = ArrayList()
    private lateinit var databaseRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentChatBinding.inflate(inflater , container , false)

        databaseRef = FirebaseDatabase.getInstance().reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManger = LinearLayoutManager(context)
        binding.rvChat.layoutManager = layoutManger
        chatAdapter = ChatAdapter()
        binding.rvChat.adapter = chatAdapter

        databaseRef.child("user").child("57").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val item = dataSnapshot.getValue(AppUser::class.java)
                    usersList.add(
                        ChatModel(item?.data?.user?.id!!, item?.data?.user?.name!!, item?.data?.user?.username!!,
                                                    item?.data?.user?.image!!, "Hello")
                    )
                    chatAdapter.submitList(usersList)
                    // Handle the retrieved item here
                } else {
                    // Item with the specified ID does not exist
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur during the retrieval
            }
        })

        //chatAdapter.submitList(arr)

        clickListener()

    }

    private fun clickListener() {
        binding.includeChat.imgProfileChat.setOnClickListener {
            navigateToProfileFragment()
        }

        chatAdapter.onItemClick = {
            lifecycleScope.launchWhenCreated {
                findNavController().navigate(
                    ChatFragmentDirections.actionChatFragmentToMessagesFragment(
                        it.name,
                        it.id.toString(),
                        viewModel.getUserId()
                    )
                )
            }
        }
    }

    private fun navigateToProfileFragment() {
        findNavController().navigate(ChatFragmentDirections.actionChatFragmentToProfileFragment())
    }
    
}