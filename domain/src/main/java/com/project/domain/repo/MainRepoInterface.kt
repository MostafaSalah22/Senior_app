package com.project.domain.repo

import androidx.lifecycle.LiveData
import com.project.domain.model.AppUser
import com.project.domain.model.ChangeResponse
import com.project.domain.model.ProfileUser
import okhttp3.MultipartBody

interface MainRepoInterface {

    suspend fun postLoginUser(username:String,password:String): AppUser
    suspend fun postRegisterUser(username: String, name: String,
                                 password: String, confirm_password:String,
                                 phone: String, email: String): AppUser

    suspend fun getProfileDataFromRemote(): ProfileUser
    suspend fun changeProfilePassword(oldPassword:String, newPassword:String, confirmPassword:String): ChangeResponse
    suspend fun changeProfileImage(file: MultipartBody.Part): ChangeResponse


    suspend fun handleLoginResponse(): LiveData<Resource<AppUser>?>
    suspend fun handleRegisterResponse(): LiveData<Resource<AppUser>?>
    suspend fun handleProfileResponse(): LiveData<Resource<ProfileUser>?>
    suspend fun handleChangePasswordResponse(): LiveData<Resource<ChangeResponse>?>
    suspend fun handleChangeImageResponse(): LiveData<Resource<ChangeResponse>?>

}