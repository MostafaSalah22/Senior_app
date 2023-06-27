package com.project.senior.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.project.domain.model.AppUser
import com.project.domain.model.ChatModel
import com.project.domain.repo.Resource
import com.project.senior.chat.recyclerview.ChatAdapter
import com.project.senior.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter


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

        val layoutManger = LinearLayoutManager(context)
        binding.rvChat.layoutManager = layoutManger
        chatAdapter = ChatAdapter()
        binding.rvChat.adapter = chatAdapter


        lifecycleScope.launchWhenCreated {
            viewModel.getChats(viewModel.getUserId().toInt())
        }

        viewModel.getChatsResponseState.observe(viewLifecycleOwner, Observer { state ->

            when (state) {
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        clickListener()

    }

    private fun clickListener() {
        /*binding.includeChat.imgProfileChat.setOnClickListener {
            navigateToProfileFragment()
        }*/

        binding.fabAddChat.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                findNavController().navigate(
                    ChatFragmentDirections.actionChatFragmentToAddChatFragment(
                        viewModel.getUserId()
                    )
                )
            }
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

    private fun successState() {
        viewModel.usersList.observe(viewLifecycleOwner, Observer {usersList ->
            //usersListFilter = usersList
            chatAdapter.submitList(usersList)
            binding.rvChat.visibility = View.VISIBLE
            binding.progressBarChat.visibility = View.GONE
        })
    }

    private fun loadingState() {
        binding.rvChat.visibility = View.GONE
        binding.progressBarChat.visibility = View.VISIBLE
    }

    private fun errorState() {
        Snackbar.make(requireView(), "Error", Snackbar.LENGTH_LONG).show()
    }
    
}