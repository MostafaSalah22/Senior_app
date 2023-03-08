package com.project.senior.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domain.model.AppUser
import com.project.domain.repo.Resource
import com.project.domain.usecase.GetProfileDataAndUpdateDataStoreUseCase
import com.project.domain.usecase.PostLoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUserUseCase: PostLoginUserUseCase,
    private val getProfileDataAndUpdateDataStoreUseCase: GetProfileDataAndUpdateDataStoreUseCase
): ViewModel() {

    private val _loginUser: MutableLiveData<AppUser> = MutableLiveData()
    val loginUser: LiveData<AppUser>
    get() = _loginUser

    private val _responseState: MutableLiveData<Resource<AppUser>?> = MutableLiveData()
    val responseState: LiveData<Resource<AppUser>?>
    get() = _responseState


    /*private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }*/

    fun postLoginUser(username:String,password:String){
        viewModelScope.launch {
            try {
                _responseState.value = Resource.Loading()
                val user = postLoginUserUseCase(username, password)!!
                _loginUser.value = user
                _responseState.value = postLoginUserUseCase.handleResponse().value
            } catch (e: Exception){
                Log.e("LoginViewModel",e.message.toString())
                _responseState.value = Resource.Error(e.message.toString())
            }
        }
    }

    suspend fun isEmailLoggedIn(): Boolean{
        return postLoginUserUseCase.isEmailLoggedIn()
    }

    suspend fun updateProfileData() {
        getProfileDataAndUpdateDataStoreUseCase()
    }
}