package com.project.senior.first

import androidx.lifecycle.ViewModel
import com.project.domain.usecase.DataStoreTypeUseCase
import com.project.domain.usecase.PostLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
    private val dataStoreTypeUseCase: DataStoreTypeUseCase,
    private val postLoginUserUseCase: PostLoginUserUseCase
): ViewModel() {

    suspend fun setUserType(type: String) {
        dataStoreTypeUseCase.setType(type)
    }

    suspend fun isEmailLoggedIn(): Boolean{
        return postLoginUserUseCase.isEmailLoggedIn()
    }
}