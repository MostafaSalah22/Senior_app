package com.project.senior.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.model.AppUser
import com.project.domain.repo.Resource
import com.project.domain.usecase.PostRegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val postRegisterUserUseCase: PostRegisterUserUseCase
): ViewModel() {

    private val _signupUser: MutableLiveData<AppUser> = MutableLiveData()
    val signupUser: LiveData<AppUser>
    get() = _signupUser

    private val _responseState: MutableLiveData<Resource<AppUser>?> = MutableLiveData()
    val responseState: LiveData<Resource<AppUser>?>
    get() = _responseState

    fun postRegisterUser(username: String, name: String,
                         password: String, confirm_password: String,
                         phone: String, email: String) {

        viewModelScope.launch {
            try {
                _responseState.value = Resource.Loading()
                val user = postRegisterUserUseCase(username, name, password, confirm_password, phone, email)!!
                _signupUser.value = user
                _responseState.value = postRegisterUserUseCase.handleResponse().value
            } catch (e: Exception){
                Log.e("SignupViewModel",e.message.toString())
                _responseState.value = Resource.Error(e.message.toString())
            }
        }
    }

}