package com.project.senior.addChat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.*
import com.project.domain.repo.Resource
import com.project.domain.usecase.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddChatViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase
): ViewModel() {

    private val _usersList: MutableLiveData<ArrayList<ChatModel>> = MutableLiveData()
    val usersList: LiveData<ArrayList<ChatModel>>
    get() = _usersList

    private val _getAllUsersResponseState: MutableLiveData<Resource<ChatUsers>?> = MutableLiveData()
    val getAllUsersResponseState: LiveData<Resource<ChatUsers>?>
    get() = _getAllUsersResponseState


    suspend fun getAllUsers() {
        try {
            _getAllUsersResponseState.value = Resource.Loading()
            _usersList.value = getAllUsersUseCase().data
            Log.i("testt1", "getAllUsers: $getAllUsersUseCase")
            _getAllUsersResponseState.value = getAllUsersUseCase.handleResponse().value
        }catch (e:Exception){
            Log.e("AddChatViewModel",e.message.toString())
            _getAllUsersResponseState.value = Resource.Error(e.message.toString())
        }
    }

}