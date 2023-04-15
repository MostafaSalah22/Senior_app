package com.project.senior.seniorProfile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domain.model.SeniorProfileData
import com.project.domain.model.SeniorProfile
import com.project.domain.repo.Resource
import com.project.domain.usecase.GetSeniorProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeniorProfileViewModel @Inject constructor(
    private val getSeniorProfileUseCase: GetSeniorProfileUseCase
): ViewModel() {

    private val _getSeniorProfileResponseState: MutableLiveData<Resource<SeniorProfile>?> = MutableLiveData()
    val getSeniorProfileResponseState: LiveData<Resource<SeniorProfile>?>
    get() = _getSeniorProfileResponseState

    private val _seniorProfile:MutableLiveData<SeniorProfileData> = MutableLiveData()
    val seniorProfile: LiveData<SeniorProfileData>
    get() = _seniorProfile

    suspend fun getSeniorProfile(userId: Int) {
        try {
            _getSeniorProfileResponseState.value = Resource.Loading()
            val response = getSeniorProfileUseCase(userId)
            _getSeniorProfileResponseState.value = getSeniorProfileUseCase.handleResponse().value
            _seniorProfile.value = response.data
        } catch (e:Exception){
            Log.e("SeniorDetailsViewModel",e.message.toString())
        }
    }
}