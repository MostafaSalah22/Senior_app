package com.project.senior.messages

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.project.senior.messages.recyclerview.MessagesAdapter
import com.project.senior.databinding.FragmentMessagesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private lateinit var messagesAdapter: MessagesAdapter
    private var receiverId: String? = null
    private var currentUserId: String? = null
    private var receiverName: String? = null
    private lateinit var databaseRef: DatabaseReference
    private val viewModel: MessagesViewModel by viewModels()

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMessagesBinding.inflate(inflater, container, false)

        databaseRef = FirebaseDatabase.getInstance().reference

        val messagesFragmentArgs by navArgs<MessagesFragmentArgs>()
        receiverId = messagesFragmentArgs.receiverId
        currentUserId = messagesFragmentArgs.currentUserId
        receiverName = messagesFragmentArgs.name

        senderRoom = receiverId + currentUserId
        receiverRoom = currentUserId + receiverId

        val layoutManger = LinearLayoutManager(context)
        binding.rvMessages.layoutManager = layoutManger
        messagesAdapter = MessagesAdapter(currentUserId!!)
        binding.rvMessages.adapter = messagesAdapter


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includeMessages.tvNameMessages.text = receiverName

        runBlocking {
            viewModel.getMessages(databaseRef, senderRoom!!)
        }

        viewModel.messagesList.observe(viewLifecycleOwner, Observer { messagesList->

            messagesAdapter.submitList(messagesList)

        })


        clickListener()
    }

    private fun clickListener(){
        binding.imgSendMessages.setOnClickListener {
            val message = binding.etTextMessages.text.toString().trim()
            if(message != "") {
                viewModel.sendMessage(
                    message,
                    databaseRef,
                    currentUserId!!,
                    senderRoom!!,
                    receiverRoom!!
                )
                binding.etTextMessages.setText("")
            }
        }
    }



    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}