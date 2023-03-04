package com.project.data.repo

import androidx.lifecycle.LiveData
import com.project.data.remote.ApiService
import com.project.domain.model.*
import com.project.domain.repo.DataStoreRepoInterface
import com.project.domain.repo.MainRepoInterface
import com.project.domain.repo.Resource
import okhttp3.MultipartBody
import retrofit2.Response

class MainRepoImpl(private val apiService: ApiService, private val dataStoreRepoInterface: DataStoreRepoInterface): MainRepoInterface {

    private lateinit var loginResponse: Response<AppUser>
    private lateinit var registerResponse: Response<AppUser>
    private lateinit var profileResponse: Response<ProfileUser>
    private lateinit var updateProfileDataResponse: Response<ProfileUser>
    private lateinit var changePasswordResponse: Response<ChangeResponse>
    private lateinit var changeImageResponse: Response<ChangeResponse>
    private lateinit var mySeniorsResponse: Response<MySeniorsResponse>

    override suspend fun postLoginUser(username: String, password: String): AppUser {
        loginResponse = apiService.postLoginUser(username, password)

        dataStoreRepoInterface.saveToDataStore("token", loginResponse.body()?.data?.token.toString())

        dataStoreRepoInterface.saveToDataStore("id",
            loginResponse.body()?.data?.user?.id.toString()
        )

        dataStoreRepoInterface.saveToDataStore("username",
            loginResponse.body()?.data?.user?.username.toString()
        )

        dataStoreRepoInterface.saveToDataStore("name",
            loginResponse.body()?.data?.user?.name.toString()
        )

        dataStoreRepoInterface.saveToDataStore("phone",
            loginResponse.body()?.data?.user?.phone.toString()
        )

        dataStoreRepoInterface.saveToDataStore("email",
            loginResponse.body()?.data?.user?.email.toString()
        )

        dataStoreRepoInterface.saveToDataStore("image",
            loginResponse.body()?.data?.user?.image.toString()
        )

        return returnTrueResponse(loginResponse)
    }

    override suspend fun isEmailLoggedIn(): Boolean {
        return !(dataStoreRepoInterface.readFromDataStore("token") == null.toString() || dataStoreRepoInterface.readFromDataStore("token") == null)
        //return dataStoreRepoInterface.readFromDataStore("token") != null.toString()
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

    override suspend fun getProfileDataFromRemoteAndUpdateDataStore() {
        profileResponse = apiService.getProfileData(dataStoreRepoInterface.readFromDataStore("token").toString())
        dataStoreRepoInterface.saveToDataStore("username",
            profileResponse.body()?.data?.username.toString()
        )

        dataStoreRepoInterface.saveToDataStore("name",
            profileResponse.body()?.data?.name.toString()
        )

        dataStoreRepoInterface.saveToDataStore("phone",
            profileResponse.body()?.data?.phone.toString()
        )

        dataStoreRepoInterface.saveToDataStore("email",
            profileResponse.body()?.data?.email.toString()
        )

        dataStoreRepoInterface.saveToDataStore("image",
            profileResponse.body()?.data?.image.toString()
        )
    }

    override suspend fun getProfileDataFromDataStore(): ProfileUser {
        return ProfileUser(data = DataX(id = dataStoreRepoInterface.readFromDataStore("id")?.toInt()!!,
                                        username = dataStoreRepoInterface.readFromDataStore("username")!!,
                                        name = dataStoreRepoInterface.readFromDataStore("name")!!,
                                        phone = dataStoreRepoInterface.readFromDataStore("phone")!!,
                                        email = dataStoreRepoInterface.readFromDataStore("email")!!,
                                        image = dataStoreRepoInterface.readFromDataStore("image")!!
                                        ), successful = true
        )
    }

    override suspend fun logout() {
        dataStoreRepoInterface.saveToDataStore("token", null.toString())
    }

    override suspend fun updateProfileData(
        name: String,
        username: String,
        phone: String,
        email: String
    ) {
        updateProfileDataResponse = apiService.updateProfileData(dataStoreRepoInterface.readFromDataStore("token").toString(), name, username, phone, email)
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
    }

    override suspend fun getMySeniorsFromRemote(): MySeniorsResponse {
        mySeniorsResponse = apiService.getMySeniors(dataStoreRepoInterface.readFromDataStore("token").toString())
        return mySeniorsResponse.body()!!
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

    override suspend fun handleUpdateProfileDataResponse(): LiveData<Resource<ProfileUser>?> {
        return handleResponse(updateProfileDataResponse)
    }

    override suspend fun handleChangePasswordResponse(): LiveData<Resource<ChangeResponse>?> {
        return handleResponse(changePasswordResponse)
    }

    override suspend fun handleChangeImageResponse(): LiveData<Resource<ChangeResponse>?> {
        return handleResponse(changeImageResponse)
    }

    override suspend fun handleGetMySeniorsResponse(): LiveData<Resource<MySeniorsResponse>?> {
        return handleResponse(mySeniorsResponse)
    }

}