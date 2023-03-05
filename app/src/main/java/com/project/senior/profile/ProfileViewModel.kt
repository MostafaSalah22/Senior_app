package com.project.senior.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.model.MiniResponse
import com.project.domain.model.ProfileUser
import com.project.domain.repo.Resource
import com.project.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val changeProfilePasswordUseCase: ChangeProfilePasswordUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val changeProfileImageUseCase: ChangeProfileImageUseCase,
    private val updateProfileDataUseCase: UpdateProfileDataUseCase,
    private val getProfileDataAndUpdateDataStoreUseCase: GetProfileDataAndUpdateDataStoreUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    private val _profileUser: MutableLiveData<ProfileUser> = MutableLiveData()
    val profileUser: LiveData<ProfileUser>
    get() = _profileUser

    private val _updateProfileDataResponseState: MutableLiveData<Resource<ProfileUser>?> = MutableLiveData()
    val updateProfileDataResponseState: LiveData<Resource<ProfileUser>?>
    get() = _updateProfileDataResponseState

    private val _changePasswordResponse: MutableLiveData<MiniResponse> = MutableLiveData()
    val changePasswordResponse: LiveData<MiniResponse>
    get() = _changePasswordResponse

    private val _changePasswordResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val changePasswordResponseState: LiveData<Resource<MiniResponse>?>
    get() = _changePasswordResponseState

    private val _changeImageResponse: MutableLiveData<MiniResponse> = MutableLiveData()
    val changeImageResponse: LiveData<MiniResponse>
    get() = _changeImageResponse

    private val _changeImageResponseState: MutableLiveData<Resource<MiniResponse>?> = MutableLiveData()
    val changeImageResponseState: LiveData<Resource<MiniResponse>?>
    get() = _changeImageResponseState


    suspend fun getProfileData() {
        viewModelScope.launch {
            try {
                //_responseState.value = Resource.Loading()
                val user = getProfileDataUseCase()
                _profileUser.value = user
                //_responseState.value = getProfileDataUseCase.handleResponse().value
            } catch (e: Exception){
                Log.e("LoginViewModel",e.message.toString())
             }
        }
    }

    suspend fun changeProfilePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                _changePasswordResponseState.value = Resource.Loading()
                val changeResponse = changeProfilePasswordUseCase(oldPassword, newPassword, confirmPassword)
                _changePasswordResponse.value = changeResponse
                _changePasswordResponseState.value = changeProfilePasswordUseCase.handleResponse().value
            } catch (e: Exception){
                Log.e("LoginViewModel",e.message.toString())
             }
        }
    }

    suspend fun changeProfileImage(file: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                _changeImageResponseState.value = Resource.Loading()
                val changeResponse = changeProfileImageUseCase(file)
                _changeImageResponse.value = changeResponse
                _changeImageResponseState.value = changeProfileImageUseCase.handleResponse().value
            } catch (e: Exception){
                Log.e("ProfileViewModel",e.message.toString())
            }
        }
    }

    suspend fun getProfileDataFromRemoteAndUpdateDataStore() {
        getProfileDataAndUpdateDataStoreUseCase()
    }

    suspend fun updateProfileData(name: String, username: String, phone: String, email: String) {
        viewModelScope.launch {
            try {
                _updateProfileDataResponseState.value = Resource.Loading()
                updateProfileDataUseCase(name, username, phone, email)
                _updateProfileDataResponseState.value =
                    updateProfileDataUseCase.handleResponse().value
            } catch (e:Exception){
                Log.e("ProfileViewModel",e.message.toString())
            }
        }
    }

    suspend fun logout() {
        logoutUseCase()
    }
}