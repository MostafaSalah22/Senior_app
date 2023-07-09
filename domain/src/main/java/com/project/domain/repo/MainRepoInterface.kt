package com.project.domain.repo

import androidx.lifecycle.LiveData
import com.project.domain.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface MainRepoInterface {

    suspend fun setUserType(type: String)
    suspend fun getUserType(): String
    suspend fun getUserId(): String
    suspend fun postLoginUser(username:String,password:String): AppUser
    suspend fun saveDataToDataStore()
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
    suspend fun deleteSenior(userId: Int): MiniResponse

    suspend fun getSchedulesFromRemote(userId: Int): SeniorSchedules
    suspend fun cancelSchedule(scheduleId: Int): MiniResponse
    suspend fun addNewSchedule(userId: Int, title: String, date: String, time: String, description: String)
    suspend fun sendNotification(userId: Int, title: String, content: String)
    suspend fun getSeniorProfile(userId: Int): SeniorProfile
    suspend fun getInformationCategories(userId: Int): ArrayList<CategoryData>?
    suspend fun deleteInformationCategory(categoryId: Int): MiniResponse
    suspend fun editInformationCategoryTitle(categoryId: Int, title: String): MiniResponse
    suspend fun addNewCategory(userId: Int, title: String): MiniResponse
    suspend fun getCategoryDetails(categoryId: Int): ArrayList<CategoryDetailsData>?
    suspend fun deleteCategoryDetails(categoryDetailsId: Int): MiniResponse
    suspend fun editCategoryDetails(categoryDetailsId: Int, title: String, description: String): MiniResponse
    suspend fun addNewCategoryDetails(categoryId: Int, userId: Int, title: String, description: String): MiniResponse
    suspend fun getBookingsData(): ArrayList<BookingsDetails>?
    suspend fun cancelBooking(bookingId: Int): MiniResponse
    suspend fun checkCode(code: String, userId: Int)
    suspend fun getAllUsers(): ChatUsers
    suspend fun getChats(userId: Int): ChatUsers
    suspend fun sendMessage(currentUserId: Int, receiverUserId: Int, message: String)
    suspend fun getMedicines(userId: Int): ArrayList<MedicineData>
    suspend fun addNewMedicine(userId: Int, medicineName:String, medicineDose: Int, medicineDescription: String)
    suspend fun deleteMedicine(medicineId: Int)
    suspend fun updateMedicine(medicineId: Int, medicineName: String, medicineDose: Int, medicineDescription: String)
    suspend fun <T:Any> handleResponse(): LiveData<Resource<T>>

}