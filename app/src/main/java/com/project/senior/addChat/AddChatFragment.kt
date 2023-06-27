package com.project.senior.addChat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.project.domain.model.ChatModel
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.addChat.recyclerview.AddChatAdapter
import com.project.senior.databinding.FragmentAddChatBinding
import com.project.senior.seniors.SeniorsViewModel
import com.project.senior.seniors.recyclerview.SeniorsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddChatFragment : Fragment() {

    private lateinit var binding: FragmentAddChatBinding
    private lateinit var usersAdapter: AddChatAdapter
    private val viewModel: AddChatViewModel by viewModels()
    private var currentUserId: String? = null
    private lateinit var usersListFilter: ArrayList<ChatModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
                FragmentAddChatBinding.inflate(inflater, container, false)

        val fragmentArgs by navArgs<AddChatFragmentArgs>()
        currentUserId = fragmentArgs.userId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.getAllUsers()
        }

        viewModel.getAllUsersResponseState.observe(viewLifecycleOwner, Observer { state ->

            when (state) {
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        val layoutManger = LinearLayoutManager(context)
        binding.rvUsersAddChat.layoutManager = layoutManger
        usersAdapter = AddChatAdapter()
        binding.rvUsersAddChat.adapter = usersAdapter

        binding.etSearchAddChat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter the items based on the search query
                val filteredItems = usersListFilter.filter {
                    it.username.contains(s.toString(), ignoreCase = true) ||
                            it.name.contains(s.toString(), ignoreCase = true)
                }
                usersAdapter.submitList(filteredItems)

            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        clickListener()
    }

    private fun clickListener(){

        usersAdapter.onItemClick = {
            findNavController().navigate(AddChatFragmentDirections.actionAddChatFragmentToMessagesFragment(
                it.name, it.id.toString(), currentUserId!!
            ))
        }
    }

    private fun successState() {
        viewModel.usersList.observe(viewLifecycleOwner, Observer {usersList ->
            usersListFilter = usersList
            usersAdapter.submitList(usersList)
            binding.rvUsersAddChat.visibility = View.VISIBLE
            binding.progressBarAddChat.visibility = View.GONE
        })
    }

    private fun loadingState() {
        binding.rvUsersAddChat.visibility = View.GONE
        binding.progressBarAddChat.visibility = View.VISIBLE
    }

    private fun errorState() {
        Snackbar.make(requireView(), "Error", Snackbar.LENGTH_LONG).show()
    }

}