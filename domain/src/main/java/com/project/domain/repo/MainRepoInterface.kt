package com.project.domain.repo

import androidx.lifecycle.LiveData
import com.project.domain.model.AppUser
import com.project.domain.model.ChangeResponse
import com.project.domain.model.ProfileUser
import okhttp3.MultipartBody
import retrofit2.http.Query

interface MainRepoInterface {

    suspend fun postLoginUser(username:String,password:String): AppUser
    suspend fun isEmailLoggedIn(): Boolean
    suspend fun postRegisterUser(username: String, name: String,
                                 password: String, confirm_password:String,
                                 phone: String, email: String): AppUser

    suspend fun getProfileDataFromRemoteAndUpdateDataStore()
    suspend fun getProfileDataFromDataStore(): ProfileUser
    suspend fun logout()
    suspend fun updateProfileData(name: String, username: String, phone: String, email: String)

    suspend fun changeProfilePassword(oldPassword:String, newPassword:String, confirmPassword:String): ChangeResponse
    suspend fun changeProfileImage(file: MultipartBody.Part): ChangeResponse

    suspend fun handleLoginResponse(): LiveData<Resource<AppUser>?>
    suspend fun handleRegisterResponse(): LiveData<Resource<AppUser>?>
    suspend fun handleProfileResponse(): LiveData<Resource<ProfileUser>?>
    suspend fun handleUpdateProfileDataResponse(): LiveData<Resource<ProfileUser>?>
    suspend fun handleChangePasswordResponse(): LiveData<Resource<ChangeResponse>?>
    suspend fun handleChangeImageResponse(): LiveData<Resource<ChangeResponse>?>

}