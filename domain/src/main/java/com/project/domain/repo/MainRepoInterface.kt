package com.project.domain.repo

import androidx.lifecycle.LiveData
import com.project.domain.model.*
import okhttp3.MultipartBody

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

    suspend fun changeProfilePassword(oldPassword:String, newPassword:String, confirmPassword:String): MiniResponse
    suspend fun changeProfileImage(file: MultipartBody.Part): MiniResponse

    suspend fun getMySeniorsFromRemote(): MySeniorsResponse
    suspend fun addNewSenior(username: String): MiniResponse

    suspend fun getSchedulesFromRemote(userId: Int): SeniorSchedules

    suspend fun handleLoginResponse(): LiveData<Resource<AppUser>?>
    suspend fun handleRegisterResponse(): LiveData<Resource<AppUser>?>
    suspend fun handleProfileResponse(): LiveData<Resource<ProfileUser>?>
    suspend fun handleUpdateProfileDataResponse(): LiveData<Resource<ProfileUser>?>
    suspend fun handleChangePasswordResponse(): LiveData<Resource<MiniResponse>?>
    suspend fun handleChangeImageResponse(): LiveData<Resource<MiniResponse>?>
    suspend fun handleGetMySeniorsResponse(): LiveData<Resource<MySeniorsResponse>?>
    suspend fun handleAddNewSeniorResponse(): LiveData<Resource<MiniResponse>?>
    suspend fun handleGetSchedulesResponse(): LiveData<Resource<SeniorSchedules>?>

}