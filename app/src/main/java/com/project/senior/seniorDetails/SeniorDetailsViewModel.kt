package com.project.senior.seniorDetails

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.MiniResponse
import com.project.domain.repo.Resource
import com.project.domain.usecase.SendNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeniorDetailsViewModel @Inject constructor(
    private val sendNotificationUseCase: SendNotificationUseCase
): ViewModel() {

    private val _sendNotificationResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val sendNotificationResponseState: LiveData<Resource<MiniResponse>?>
    get() = _sendNotificationResponseState

    suspend fun sendNotification(userId: Int, title: String, content:String) {
        try {
            _sendNotificationResponseState.value = Resource.Loading()
            sendNotificationUseCase(userId, title, content)
            _sendNotificationResponseState.value = sendNotificationUseCase.handleResponse().value
        } catch (e:Exception){
            Log.e("SeniorDetailsViewModel",e.message.toString())
        }
    }
}