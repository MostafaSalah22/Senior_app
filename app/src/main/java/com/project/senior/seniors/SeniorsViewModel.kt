package com.project.senior.seniors

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.MiniResponse
import com.project.domain.model.MySeniorsData
import com.project.domain.model.MySeniorsResponse
import com.project.domain.repo.Resource
import com.project.domain.usecase.AddNewSeniorUseCase
import com.project.domain.usecase.DeleteSeniorUseCase
import com.project.domain.usecase.GetMySeniorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeniorsViewModel @Inject constructor(
    private val getMySeniorsUseCase: GetMySeniorsUseCase,
    private val addNewSeniorUseCase: AddNewSeniorUseCase,
    private val deleteSeniorUseCase: DeleteSeniorUseCase
    ): ViewModel() {

    private val _seniorsList: MutableLiveData<ArrayList<MySeniorsData>> = MutableLiveData()
    val seniorsList: LiveData<ArrayList<MySeniorsData>>
    get() = _seniorsList

    private val _getSeniorsResponseState: MutableLiveData<Resource<MySeniorsResponse>?> = MutableLiveData()
    val getSeniorsResponseState: LiveData<Resource<MySeniorsResponse>?>
    get() = _getSeniorsResponseState

    private val _addSeniorMessage: MutableLiveData<String> = MutableLiveData()
    val addSeniorMessage: LiveData<String>
    get() = _addSeniorMessage

    private val _deleteSeniorResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val deleteSeniorResponseState: LiveData<Resource<MiniResponse>?>
    get() = _deleteSeniorResponseState

    private val _deleteSeniorMessage: MutableLiveData<String> = MutableLiveData()
    val deleteSeniorMessage: LiveData<String>
    get() = _deleteSeniorMessage

    private val _addSeniorResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val addSeniorResponseState: LiveData<Resource<MiniResponse>?>
        get() = _addSeniorResponseState

    suspend fun getMySeniors() {
        try {
            _getSeniorsResponseState.value = Resource.Loading()
            _seniorsList.value = getMySeniorsUseCase().data
            _getSeniorsResponseState.value = getMySeniorsUseCase.handleResponse().value
        }catch (e:Exception){
            Log.e("SeniorsViewModel",e.message.toString())
        }
    }

    suspend fun addNewSenior(username: String) {
        try {
            _addSeniorResponseState.value = Resource.Loading()
            _addSeniorMessage.value = addNewSeniorUseCase(username).message
            _addSeniorResponseState.value = addNewSeniorUseCase.handleResponse().value
        }catch (e:Exception){
            Log.e("SeniorsViewModel",e.message.toString())
        }
    }

    suspend fun deleteSenior(userId: Int) {
        try {
            _deleteSeniorResponseState.value = Resource.Loading()
            _deleteSeniorMessage.value = deleteSeniorUseCase(userId).message
            _deleteSeniorResponseState.value = deleteSeniorUseCase.handleResponse().value
        }catch (e:Exception){
            Log.e("SeniorsViewModel",e.message.toString())
        }
    }
}