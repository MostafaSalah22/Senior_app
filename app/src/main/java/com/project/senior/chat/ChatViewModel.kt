package com.project.senior.chat

import androidx.lifecycle.ViewModel
import com.project.domain.usecase.GetUserIdFromDataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getUserIdFromDataStoreUseCase: GetUserIdFromDataStoreUseCase
): ViewModel() {


    suspend fun getUserId(): String{
        return getUserIdFromDataStoreUseCase()
    }
}