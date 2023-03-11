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
    private lateinit var changePasswordResponse: Response<MiniResponse>
    private lateinit var changeImageResponse: Response<MiniResponse>
    private lateinit var mySeniorsResponse: Response<MySeniorsResponse>
    private lateinit var addNewSeniorResponse: Response<MiniResponse>
    private lateinit var deleteSeniorResponse: Response<MiniResponse>
    private lateinit var seniorSchedulesResponse: Response<SeniorSchedules>
    private lateinit var cancelScheduleResponse: Response<MiniResponse>
    private lateinit var addNewScheduleResponse: Response<MiniResponse>
    private lateinit var sendNotificationResponse: Response<MiniResponse>

    private val TOKEN = dataStoreRepoInterface.readFromDataStore("token").toString()

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
        profileResponse = apiService.getProfileData(TOKEN)
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
        updateProfileDataResponse = apiService.updateProfileData(TOKEN, name, username, phone, email)
    }


    override suspend fun changeProfilePassword(oldPassword:String, newPassword:String, confirmPassword:String): MiniResponse {
        changePasswordResponse = apiService.changeProfilePassword(TOKEN,
                                                            oldPassword, newPassword, confirmPassword)

        return returnChangeTrueResponse(changePasswordResponse)
    }

    override suspend fun changeProfileImage(file: MultipartBody.Part): MiniResponse {
        val token = TOKEN
        changeImageResponse = apiService.changeProfileImage(token, file)
        return returnChangeTrueResponse(changeImageResponse)
    }

    override suspend fun getMySeniorsFromRemote(): MySeniorsResponse {
        mySeniorsResponse = apiService.getMySeniors(TOKEN)
        return mySeniorsResponse.body()!!
    }

    override suspend fun addNewSenior(username: String): MiniResponse {
        addNewSeniorResponse = apiService.addNewSenior(TOKEN, username)
        return returnChangeTrueResponse(addNewSeniorResponse)
    }

    override suspend fun deleteSenior(userId: Int): MiniResponse {
        deleteSeniorResponse = apiService.deleteSenior(TOKEN, userId)
        return returnChangeTrueResponse(deleteSeniorResponse)
    }

    override suspend fun getSchedulesFromRemote(userId: Int): SeniorSchedules {
        seniorSchedulesResponse = apiService.getSchedules(TOKEN,
                                                            userId)
        return seniorSchedulesResponse.body()!!
    }

    override suspend fun cancelSchedule(scheduleId: Int): MiniResponse {
        cancelScheduleResponse = apiService.cancelSchedule(TOKEN,
                                                                scheduleId)
        return returnChangeTrueResponse(cancelScheduleResponse)
    }

    override suspend fun addNewSchedule(
        userId: Int,
        title: String,
        date: String,
        time: String,
        description: String
    ) {
        addNewScheduleResponse = apiService.addNewSchedule(TOKEN,
                                                            userId, title, date, time, 1, description)

        //returnChangeTrueResponse(addNewScheduleResponse)
    }

    override suspend fun sendNotification(userId: Int, title: String, content: String) {
        sendNotificationResponse = apiService.sendNotification(TOKEN, userId, title, content)
        //returnChangeTrueResponse(sendNotificationResponse)
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

    override suspend fun handleChangePasswordResponse(): LiveData<Resource<MiniResponse>?> {
        return handleResponse(changePasswordResponse)
    }

    override suspend fun handleChangeImageResponse(): LiveData<Resource<MiniResponse>?> {
        return handleResponse(changeImageResponse)
    }

    override suspend fun handleGetMySeniorsResponse(): LiveData<Resource<MySeniorsResponse>?> {
        return handleResponse(mySeniorsResponse)
    }

    override suspend fun handleAddNewSeniorResponse(): LiveData<Resource<MiniResponse>?> {
        return handleResponse(addNewSeniorResponse)
    }

    override suspend fun handleDeleteSeniorResponse(): LiveData<Resource<MiniResponse>?> {
        return handleResponse(deleteSeniorResponse)
    }

    override suspend fun handleGetSchedulesResponse(): LiveData<Resource<SeniorSchedules>?> {
        return handleResponse(seniorSchedulesResponse)
    }

    override suspend fun handleCancelScheduleResponse(): LiveData<Resource<MiniResponse>?> {
        return handleResponse(cancelScheduleResponse)
    }

    override suspend fun handleAddNewScheduleResponse(): LiveData<Resource<MiniResponse>?> {
        return handleResponse(addNewScheduleResponse)
    }

    override suspend fun handleSendNotificationResponse(): LiveData<Resource<MiniResponse>?> {
        return handleResponse(sendNotificationResponse)
    }


}