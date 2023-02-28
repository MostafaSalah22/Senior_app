package com.project.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.project.data.remote.ApiService
import com.project.domain.model.AppUser
import com.project.domain.model.ChangeResponse
import com.project.domain.model.ProfileUser
import com.project.domain.repo.DataStoreRepoInterface
import com.project.domain.repo.MainRepoInterface
import com.project.domain.repo.Resource
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepoImpl(private val apiService: ApiService, private val dataStoreRepoInterface: DataStoreRepoInterface): MainRepoInterface {

    private lateinit var loginResponse: Response<AppUser>
    private lateinit var registerResponse: Response<AppUser>
    private lateinit var profileResponse: Response<ProfileUser>
    private lateinit var changePasswordResponse: Response<ChangeResponse>
    private lateinit var changeImageResponse: Response<ChangeResponse>

    override suspend fun postLoginUser(username: String, password: String): AppUser {
        loginResponse = apiService.postLoginUser(username, password)
        dataStoreRepoInterface.saveToDataStore("token", loginResponse.body()?.data?.token.toString())
        return returnTrueResponse(loginResponse)
    }

    override suspend fun postRegisterUser(
        username: String,
        name: String,
        password: String,
        confirm_password: String,
        phone: String,
        email: String
    ): AppUser {
        registerResponse = apiService.postRegisterUser(username, name, password, confirm_password, phone, email)
        return returnTrueResponse(registerResponse)
    }

    override suspend fun getProfileDataFromRemote(): ProfileUser {
        profileResponse = apiService.getProfileData(dataStoreRepoInterface.readFromDataStore("token").toString())
        return profileResponse.body()!!
    }

    override suspend fun changeProfilePassword(oldPassword:String, newPassword:String, confirmPassword:String): ChangeResponse {
        changePasswordResponse = apiService.changeProfilePassword(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                            oldPassword, newPassword, confirmPassword)

        return returnChangeTrueResponse(changePasswordResponse)
    }

    override suspend fun changeProfileImage(file: MultipartBody.Part): ChangeResponse {
        val token = dataStoreRepoInterface.readFromDataStore("token").toString()
        changeImageResponse = apiService.changeProfileImage(token, file)
        return returnChangeTrueResponse(changeImageResponse)
        //val test = returnChangeTrueResponse(response)
    }

    override suspend fun handleLoginResponse(): LiveData<Resource<AppUser>?> {
        return handleResponse(loginResponse)
    }

    override suspend fun handleRegisterResponse(): LiveData<Resource<AppUser>?> {
        return handleResponse(registerResponse)
    }

    override suspend fun handleProfileResponse(): LiveData<Resource<ProfileUser>?> {
        return handleResponse(profileResponse)
    }

    override suspend fun handleChangePasswordResponse(): LiveData<Resource<ChangeResponse>?> {
        return handleResponse(changePasswordResponse)
    }

    override suspend fun handleChangeImageResponse(): LiveData<Resource<ChangeResponse>?> {
        return handleResponse(changeImageResponse)
    }

}