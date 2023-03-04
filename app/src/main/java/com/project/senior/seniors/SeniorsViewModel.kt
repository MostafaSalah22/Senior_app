package com.project.senior.seniors

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.MySeniorsData
import com.project.domain.model.MySeniorsResponse
import com.project.domain.repo.Resource
import com.project.domain.usecase.GetMySeniorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeniorsViewModel @Inject constructor(
    private val getMySeniorsUseCase: GetMySeniorsUseCase
    ): ViewModel() {

    private val _seniorsList: MutableLiveData<ArrayList<MySeniorsData>> = MutableLiveData()
    val seniorsList: LiveData<ArrayList<MySeniorsData>>
    get() = _seniorsList

    private val _getSeniorsResponseState: MutableLiveData<Resource<MySeniorsResponse>?> = MutableLiveData()
    val getSeniorsResponseState: LiveData<Resource<MySeniorsResponse>?>
    get() = _getSeniorsResponseState

    suspend fun getMySeniors() {
        try {
            _getSeniorsResponseState.value = Resource.Loading()
            _seniorsList.value = getMySeniorsUseCase().data
            _getSeniorsResponseState.value = getMySeniorsUseCase.handleResponse().value
        }catch (e:Exception){
            Log.e("SeniorsViewModel",e.message.toString())
        }
    }
}