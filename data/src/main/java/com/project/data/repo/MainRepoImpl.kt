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
    private lateinit var getSeniorProfileResponse: Response<SeniorProfile>
    private lateinit var getInformationCategoriesResponse: Response<InformationCategories>
    private lateinit var deleteInformationCategoryResponse: Response<MiniResponse>

    //private val TOKEN = dataStoreRepoInterface.readFromDataStore("token").toString()
    //private val TYPE = dataStoreRepoInterface.readFromDataStore("type")
    override suspend fun setUserType(type: String) {
        dataStoreRepoInterface.saveToDataStore("type", type)
    }

    override suspend fun getUserType(): String {
        return dataStoreRepoInterface.readFromDataStore("type").toString()
    }

    override suspend fun postLoginUser(username: String, password: String): AppUser {
        loginResponse = if(dataStoreRepoInterface.readFromDataStore("type") == "user")
            apiService.postLoginUser(username, password)
        else
            apiService.postLoginDoctor(username, password)

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
        registerResponse = if (dataStoreRepoInterface.readFromDataStore("type")  == "user")
                                apiService.postRegisterUser(username, name, password, confirm_password, phone, email)
                           else
                                apiService.postRegisterUserDoctor(username, name, password, confirm_password, phone, email)
        return returnTrueResponse(registerResponse)
    }

    override suspend fun getProfileDataFromRemoteAndUpdateDataStore() {
        profileResponse = if (dataStoreRepoInterface.readFromDataStore("type") == "user")
                            apiService.getProfileData(dataStoreRepoInterface.readFromDataStore("token").toString())
                          else
                            apiService.getProfileDataDoctor(dataStoreRepoInterface.readFromDataStore("token").toString())
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
        updateProfileDataResponse = if (dataStoreRepoInterface.readFromDataStore("type") == "user")
                                        apiService.updateProfileData(dataStoreRepoInterface.readFromDataStore("token").toString(), name, username, phone, email)
                                    else
                                        apiService.updateProfileDataDoctor(dataStoreRepoInterface.readFromDataStore("token").toString(), name, username, phone, email)
    }


    override suspend fun changeProfilePassword(oldPassword:String, newPassword:String, confirmPassword:String): MiniResponse {
        changePasswordResponse = if (dataStoreRepoInterface.readFromDataStore("type") == "user")
                                    apiService.changeProfilePassword(dataStoreRepoInterface.readFromDataStore("token").toString(), oldPassword, newPassword, confirmPassword)
                                 else
                                    apiService.changeProfilePasswordDoctor(dataStoreRepoInterface.readFromDataStore("token").toString(), oldPassword, newPassword, confirmPassword)

        return returnChangeTrueResponse(changePasswordResponse)
    }

    override suspend fun changeProfileImage(file: MultipartBody.Part): MiniResponse {
        val token = dataStoreRepoInterface.readFromDataStore("token").toString()
        changeImageResponse = if (dataStoreRepoInterface.readFromDataStore("type") == "user")
                                apiService.changeProfileImage(token, file)
                              else
                                apiService.changeProfileImageDoctor(token, file)
        return returnChangeTrueResponse(changeImageResponse)
    }

    override suspend fun getMySeniorsFromRemote(): MySeniorsResponse {
        mySeniorsResponse = apiService.getMySeniors(dataStoreRepoInterface.readFromDataStore("token").toString())
        return mySeniorsResponse.body()!!
    }

    override suspend fun addNewSenior(username: String): MiniResponse {
        addNewSeniorResponse = apiService.addNewSenior(dataStoreRepoInterface.readFromDataStore("token").toString(), username)
        return returnChangeTrueResponse(addNewSeniorResponse)
    }

    override suspend fun deleteSenior(userId: Int): MiniResponse {
        deleteSeniorResponse = apiService.deleteSenior(dataStoreRepoInterface.readFromDataStore("token").toString(), userId)
        return returnChangeTrueResponse(deleteSeniorResponse)
    }

    override suspend fun getSchedulesFromRemote(userId: Int): SeniorSchedules {
        seniorSchedulesResponse = apiService.getSchedules(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                            userId)
        return seniorSchedulesResponse.body()!!
    }

    override suspend fun cancelSchedule(scheduleId: Int): MiniResponse {
        cancelScheduleResponse = apiService.cancelSchedule(dataStoreRepoInterface.readFromDataStore("token").toString(),
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
        addNewScheduleResponse = apiService.addNewSchedule(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                            userId, title, date, time, 1, description)

        //returnChangeTrueResponse(addNewScheduleResponse)
    }

    override suspend fun sendNotification(userId: Int, title: String, content: String) {
        sendNotificationResponse = apiService.sendNotification(dataStoreRepoInterface.readFromDataStore("token").toString(), userId, title, content)
        //returnChangeTrueResponse(sendNotificationResponse)
    }

    override suspend fun getSeniorProfile(userId: Int): SeniorProfile {
        getSeniorProfileResponse = apiService.getSeniorProfile(userId)
        return getSeniorProfileResponse.body()!!
    }

    override suspend fun getInformationCategories(userId: Int): ArrayList<CategoryData>? {
        getInformationCategoriesResponse = apiService.getInformationCategories(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                                    userId)
        return getInformationCategoriesResponse.body()?.data
    }

    override suspend fun deleteInformationCategory(categoryId: Int): MiniResponse {
        deleteInformationCategoryResponse = apiService.deleteInformationCategory(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                                    categoryId)
        return deleteInformationCategoryResponse.body()!!
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

    override suspend fun handleGetSeniorProfileResponse(): LiveData<Resource<SeniorProfile>?> {
        return handleResponse(getSeniorProfileResponse)
    }

    override suspend fun handleGetInformationCategoriesResponse(): LiveData<Resource<InformationCategories>?> {
        return handleResponse(getInformationCategoriesResponse)
    }

    override suspend fun handleDeleteInformationCategoryResponse(): LiveData<Resource<MiniResponse>?> {
        return handleResponse(deleteInformationCategoryResponse)
    }


}