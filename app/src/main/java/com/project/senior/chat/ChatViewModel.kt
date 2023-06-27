package com.project.senior.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.ChatModel
import com.project.domain.model.ChatUsers
import com.project.domain.repo.Resource
import com.project.domain.usecase.GetChatsUseCase
import com.project.domain.usecase.GetUserIdFromDataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getUserIdFromDataStoreUseCase: GetUserIdFromDataStoreUseCase,
    private val getChatsUseCase: GetChatsUseCase
): ViewModel() {

    private val _usersList: MutableLiveData<ArrayList<ChatModel>> = MutableLiveData()
    val usersList: LiveData<ArrayList<ChatModel>>
    get() = _usersList

    private val _getChatsResponseState: MutableLiveData<Resource<ChatUsers>?> = MutableLiveData()
    val getChatsResponseState: LiveData<Resource<ChatUsers>?>
    get() = _getChatsResponseState


    suspend fun getChats(userId: Int) {
        try {
            _getChatsResponseState.value = Resource.Loading()
            _usersList.value = getChatsUseCase(userId).data
            //Log.i("testt1", "getAllUsers: $getAllUsersUseCase")
            _getChatsResponseState.value = getChatsUseCase.handleResponse().value
        }catch (e:Exception){
            Log.e("AddChatViewModel",e.message.toString())
            _getChatsResponseState.value = Resource.Error(e.message.toString())
        }
    }


    suspend fun getUserId(): String{
        return getUserIdFromDataStoreUseCase()
    }
}