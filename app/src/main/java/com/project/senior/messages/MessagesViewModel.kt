package com.project.senior.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.project.domain.model.Message
import com.project.domain.usecase.GetUserIdFromDataStoreUseCase
import com.project.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.http.Query
import javax.inject.Inject
import kotlin.random.Random
@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {


    fun sendMessage(message: String, databaseRef: DatabaseReference, currentUserId: String, senderRoom: String, receiverRoom: String){
        val messageObject = Message(message = message, senderId = currentUserId.toLong(), id = Random.nextInt())

        databaseRef.child("chats").child(senderRoom).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                databaseRef.child("chats").child(receiverRoom).child("messages").push()
                    .setValue(messageObject)
            }

    }

    private val _messagesList: MutableLiveData<ArrayList<Message>> = MutableLiveData()
    val messagesList: LiveData<ArrayList<Message>>
    get() = _messagesList

    fun getMessages(databaseRef: DatabaseReference, senderRoom: String) {
        databaseRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messagesListViewModel: ArrayList<Message> = ArrayList()
                    messagesListViewModel.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messagesListViewModel.add(message!!)
                    }
                    _messagesList.value = messagesListViewModel
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    suspend fun sendMessage(currentUserId: Int, receiverUserId: Int, message: String){
        sendMessageUseCase(currentUserId, receiverUserId, message)
    }
}