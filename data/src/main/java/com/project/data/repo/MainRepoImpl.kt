package com.project.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.project.data.remote.ApiService
import com.project.domain.model.*
import com.project.domain.repo.DataStoreRepoInterface
import com.project.domain.repo.MainRepoInterface
import com.project.domain.repo.Resource
import okhttp3.MultipartBody
import retrofit2.Response

class MainRepoImpl(private val apiService: ApiService, private val dataStoreRepoInterface: DataStoreRepoInterface): MainRepoInterface {

    private lateinit var response: Response<*>

    override suspend fun setUserType(type: String) {
        dataStoreRepoInterface.saveToDataStore("type", type)
    }

    override suspend fun getUserType(): String {
        return dataStoreRepoInterface.readFromDataStore("type").toString()
    }

    override suspend fun getUserId(): String {
        return dataStoreRepoInterface.readFromDataStore("id").toString()
    }

    override suspend fun postLoginUser(username: String, password: String): AppUser {
        response = if(dataStoreRepoInterface.readFromDataStore("type") == "user")
            apiService.postLoginUser(username, password)
        else
            apiService.postLoginDoctor(username, password)

        dataStoreRepoInterface.saveToDataStore("token", (response.body() as AppUser).data?.token.toString())

        dataStoreRepoInterface.saveToDataStore("id",
            (response.body() as AppUser).data?.user?.id.toString()
        )

        dataStoreRepoInterface.saveToDataStore("username",
            (response.body() as AppUser).data?.user?.username.toString()
        )

        dataStoreRepoInterface.saveToDataStore("name",
            (response.body() as AppUser).data?.user?.name.toString()
        )

        dataStoreRepoInterface.saveToDataStore("phone",
            (response.body() as AppUser).data?.user?.phone.toString()
        )

        dataStoreRepoInterface.saveToDataStore("email",
            (response.body() as AppUser).data?.user?.email.toString()
        )

        dataStoreRepoInterface.saveToDataStore("image",
            (response.body() as AppUser).data?.user?.image.toString()
        )

        return returnTrueResponse(response as Response<AppUser>)
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
        response = if (dataStoreRepoInterface.readFromDataStore("type")  == "user")
                                apiService.postRegisterUser(username, name, password, confirm_password, phone, email)
                           else
                                apiService.postRegisterUserDoctor(username, name, password, confirm_password, phone, email)
        return returnTrueResponse(response as Response<AppUser>)
    }

    override suspend fun getProfileDataFromRemoteAndUpdateDataStore() {
        response = if (dataStoreRepoInterface.readFromDataStore("type") == "user")
                            apiService.getProfileData(dataStoreRepoInterface.readFromDataStore("token").toString())
                          else
                            apiService.getProfileDataDoctor(dataStoreRepoInterface.readFromDataStore("token").toString())
        dataStoreRepoInterface.saveToDataStore("username",
            (response.body() as ProfileUser).data.username
        )

        dataStoreRepoInterface.saveToDataStore("name",
            (response.body() as ProfileUser).data.name
        )

        dataStoreRepoInterface.saveToDataStore("phone",
            (response.body() as ProfileUser).data.phone
        )

        dataStoreRepoInterface.saveToDataStore("email",
            (response.body() as ProfileUser).data.email
        )

        dataStoreRepoInterface.saveToDataStore("image",
            (response.body() as ProfileUser).data.image
        )
    }

    override suspend fun getProfileDataFromDataStore(): ProfileUser {
        return ProfileUser(data = ProfileData(id = dataStoreRepoInterface.readFromDataStore("id")?.toInt()!!,
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
        response = if (dataStoreRepoInterface.readFromDataStore("type") == "user")
                                        apiService.updateProfileData(dataStoreRepoInterface.readFromDataStore("token").toString(), name, username, phone, email)
                                    else
                                        apiService.updateProfileDataDoctor(dataStoreRepoInterface.readFromDataStore("token").toString(), name, username, phone, email)
    }


    override suspend fun changeProfilePassword(oldPassword:String, newPassword:String, confirmPassword:String): MiniResponse {
        response = if (dataStoreRepoInterface.readFromDataStore("type") == "user")
                                    apiService.changeProfilePassword(dataStoreRepoInterface.readFromDataStore("token").toString(), oldPassword, newPassword, confirmPassword)
                                 else
                                    apiService.changeProfilePasswordDoctor(dataStoreRepoInterface.readFromDataStore("token").toString(), oldPassword, newPassword, confirmPassword)

        return returnChangeTrueResponse(response as Response<MiniResponse>)
    }

    override suspend fun changeProfileImage(file: MultipartBody.Part): MiniResponse {
        val token = dataStoreRepoInterface.readFromDataStore("token").toString()
        response = if (dataStoreRepoInterface.readFromDataStore("type") == "user")
                                apiService.changeProfileImage(token, file)
                              else
                                apiService.changeProfileImageDoctor(token, file)
        return returnChangeTrueResponse(response as Response<MiniResponse>)
    }

    override suspend fun getMySeniorsFromRemote(): MySeniorsResponse {
        response = apiService.getMySeniors(dataStoreRepoInterface.readFromDataStore("token").toString())
        return response.body() as MySeniorsResponse
    }

    override suspend fun addNewSenior(username: String): MiniResponse {
        response = apiService.addNewSenior(dataStoreRepoInterface.readFromDataStore("token").toString(), username)
        return returnChangeTrueResponse(response as Response<MiniResponse>)
    }

    override suspend fun deleteSenior(userId: Int): MiniResponse {
        response = apiService.deleteSenior(dataStoreRepoInterface.readFromDataStore("token").toString(), userId)
        return returnChangeTrueResponse(response as Response<MiniResponse>)
    }

    override suspend fun getSchedulesFromRemote(userId: Int): SeniorSchedules {
        response = apiService.getSchedules(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                            userId)
        return response.body() as SeniorSchedules
    }

    override suspend fun cancelSchedule(scheduleId: Int): MiniResponse {
        response = apiService.cancelSchedule(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                scheduleId)
        return returnChangeTrueResponse(response as Response<MiniResponse>)
    }

    override suspend fun addNewSchedule(
        userId: Int,
        title: String,
        date: String,
        time: String,
        description: String
    ) {
        response = apiService.addNewSchedule(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                            userId, title, date, time, 1, description)

        //returnChangeTrueResponse(addNewScheduleResponse)
    }

    override suspend fun sendNotification(userId: Int, title: String, content: String) {
        response = apiService.sendNotification(dataStoreRepoInterface.readFromDataStore("token").toString(), userId, title, content)
        //returnChangeTrueResponse(sendNotificationResponse)
    }

    override suspend fun getSeniorProfile(userId: Int): SeniorProfile {
        response = apiService.getSeniorProfile(userId)
        return response.body() as SeniorProfile
    }

    override suspend fun getInformationCategories(userId: Int): ArrayList<CategoryData>? {
        response = apiService.getInformationCategories(userId)
        return (response.body() as InformationCategories).data
    }

    override suspend fun deleteInformationCategory(categoryId: Int): MiniResponse {
        response = apiService.deleteInformationCategory(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                                    categoryId)
        return response.body() as MiniResponse
    }

    override suspend fun editInformationCategoryTitle(
        categoryId: Int,
        title: String
    ): MiniResponse {
        response = apiService.editInformationCategoryTitle(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                                            categoryId, title)
        return response.body() as MiniResponse
    }

    override suspend fun addNewCategory(userId: Int, title: String): MiniResponse {
        response = apiService.addNewCategory(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                userId, title)
        return response.body() as MiniResponse
    }

    override suspend fun getCategoryDetails(categoryId: Int): ArrayList<CategoryDetailsData>? {
        response = apiService.getCategoryDetails(categoryId)
        return (response.body() as CategoryDetails).data
    }

    override suspend fun deleteCategoryDetails(categoryDetailsId: Int): MiniResponse {
        response = apiService.deleteCategoryDetails(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                                categoryDetailsId)

        return response.body() as MiniResponse
    }

    override suspend fun editCategoryDetails(
        categoryDetailsId: Int,
        title: String,
        description: String
    ): MiniResponse {
        response = apiService.editCategoryDetails(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                                categoryDetailsId, title, description)

        return response.body() as MiniResponse
    }

    override suspend fun addNewCategoryDetails(
        categoryId: Int,
        userId: Int,
        title: String,
        description: String
    ): MiniResponse {
        response = apiService.addNewCategoryDetails(dataStoreRepoInterface.readFromDataStore("token").toString(),
                                                                            categoryId, userId, title, description)
        return response.body() as MiniResponse
    }

    override suspend fun getBookingsData(): ArrayList<BookingsDetails>? {
        response = apiService.getBookingsData(dataStoreRepoInterface.readFromDataStore("token").toString())

        return (response.body() as BookingsData).data
    }

    override suspend fun cancelBooking(bookingId: Int): MiniResponse {
        response = apiService.cancelBooking(dataStoreRepoInterface.readFromDataStore("token").toString(), bookingId)

        return response.body() as MiniResponse
    }

    override suspend fun checkCode(code: String, userId: Int) {
        response = apiService.checkCode(dataStoreRepoInterface.readFromDataStore("token").toString(), code, userId)
    }

    override suspend fun getAllUsers(): ChatUsers {
        response = apiService.getAllUsers()
        return response.body() as ChatUsers
    }

    override suspend fun getChats(userId: Int): ChatUsers {
        response = apiService.getChats(userId)
        //Log.i("testt1", "getChats: ${response.body()}")
        return response.body() as ChatUsers
    }

    override suspend fun sendMessage(currentUserId: Int, receiverUserId: Int, message: String) {
        response = apiService.sendMessage(currentUserId, receiverUserId, message)
    }

    override suspend fun <T : Any> handleResponse(): LiveData<Resource<T>> {
        return handleResponse(response) as LiveData<Resource<T>>
    }


}