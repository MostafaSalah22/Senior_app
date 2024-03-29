package com.project.data.remote

import android.service.autofill.FillEventHistory
import com.project.domain.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("care-takers/login")
    suspend fun postLoginUser(@Query("username")username:String,
                              @Query("password")password:String): Response<AppUser>

    @POST("doctors/login")
    suspend fun postLoginDoctor(@Query("username")username:String,
                                @Query("password")password:String): Response<AppUser>

    @POST("care-takers/register")
    suspend fun postRegisterUser(@Query("username")username: String,
                                 @Query("name")name: String,
                                 @Query("password")password: String,
                                 @Query("confirm_password")confirm_password: String,
                                 @Query("phone")phone: String,
                                 @Query("email")email: String): Response<AppUser>

    @POST("doctors/register")
    suspend fun postRegisterUserDoctor(@Query("username")username: String,
                                 @Query("name")name: String,
                                 @Query("password")password: String,
                                 @Query("confirm_password")confirm_password: String,
                                 @Query("phone")phone: String,
                                 @Query("email")email: String): Response<AppUser>

    @GET("care-takers/profile")
    suspend fun getProfileData(@Query("token")token:String): Response<ProfileUser>

    @GET("doctors/profile")
    suspend fun getProfileDataDoctor(@Query("token")token:String): Response<ProfileUser>

    @POST("care-takers/profile/changePassword")
    suspend fun changeProfilePassword(@Query("token")token:String,
                                      @Query("old_password")oldPassword:String,
                                      @Query("password")newPassword:String,
                                      @Query("confirm_password")confirmPassword:String):Response<MiniResponse>

    @POST("doctors/profile/changePassword")
    suspend fun changeProfilePasswordDoctor(@Query("token")token:String,
                                            @Query("old_password")oldPassword:String,
                                            @Query("password")newPassword:String,
                                            @Query("confirm_password")confirmPassword:String):Response<MiniResponse>

    @Multipart
    @POST("care-takers/profile/changeImage")
    suspend fun changeProfileImage(@Query("token") token: String,
                                   @Part image: MultipartBody.Part):Response<MiniResponse>

    @Multipart
    @POST("doctors/profile/changeImage")
    suspend fun changeProfileImageDoctor(@Query("token") token: String,
                                        @Part image: MultipartBody.Part):Response<MiniResponse>

    @POST("care-takers/profile/update")
    suspend fun updateProfileData(@Query("token") token:String,
                                  @Query("name") name: String,
                                  @Query("username") username: String,
                                  @Query("phone") phone: String,
                                  @Query("email") email: String):Response<ProfileUser>

    @POST("doctors/profile/update")
    suspend fun updateProfileDataDoctor(@Query("token") token:String,
                                        @Query("name") name: String,
                                        @Query("username") username: String,
                                        @Query("phone") phone: String,
                                        @Query("email") email: String):Response<ProfileUser>

    @GET("care-takers/my-senior")
    suspend fun getMySeniors(@Query("token") token: String): Response<MySeniorsResponse>

    @POST("care-takers/my-senior/link")
    suspend fun addNewSenior(@Query("token") token: String,
                             @Query("username") username: String): Response<MiniResponse>

    @POST("care-takers/my-senior/remove")
    suspend fun deleteSenior(@Query("token") token: String,
                                @Query("user_id") userId: Int): Response<MiniResponse>

    @GET("care-takers/schedules")
    suspend fun getSchedules(@Query("token") token: String,
                             @Query("user_id") userId: Int): Response<SeniorSchedules>

    @POST("care-takers/schedules/cancel")
    suspend fun cancelSchedule(@Query("token") token: String,
                               @Query("schedule_id") scheduleId: Int): Response<MiniResponse>

    @POST("care-takers/schedules/create")
    suspend fun addNewSchedule(@Query("token") token: String,
                                @Query("user_id") userId: Int,
                                @Query("title") title: String,
                                @Query("date") date: String,
                                @Query("time") time: String,
                                @Query("type") type: Int,
                                @Query("description") description: String): Response<MiniResponse>

    @POST("care-takers/notifications/create")
    suspend fun sendNotification(@Query("token") token: String,
                                 @Query("user_id") userId: Int,
                                 @Query("title") title: String,
                                 @Query("content") content: String): Response<MiniResponse>

    @GET("care-takers/history-categories")
    suspend fun getInformationCategories(@Query("user_id") userId: Int): Response<InformationCategories>

    @POST("care-takers/history-categories/delete")
    suspend fun deleteInformationCategory(@Query("token") token: String,
                                          @Query("history_category_id") categoryId: Int): Response<MiniResponse>
    @POST("care-takers/history-categories/update")
    suspend fun editInformationCategoryTitle(@Query("token") token: String,
                                             @Query("history_category_id") categoryId: Int,
                                             @Query("title") title: String): Response<MiniResponse>
    @POST("care-takers/history-categories/create")
    suspend fun addNewCategory(@Query("token") token: String,
                               @Query("user_id") userId: Int,
                               @Query("title") title: String): Response<MiniResponse>

    @GET("care-takers/history")
    suspend fun getCategoryDetails(@Query("history_category_id") categoryId: Int): Response<CategoryDetails>

    @POST("care-takers/history/delete")
    suspend fun deleteCategoryDetails(@Query("token") token: String,
                                      @Query("history_id") categoryDetailsId: Int): Response<MiniResponse>

    @POST("care-takers/history/update")
    suspend fun editCategoryDetails(@Query("token") token: String,
                                    @Query("history_id") categoryDetailsId: Int,
                                    @Query("title") title: String,
                                    @Query("description") description: String): Response<MiniResponse>

    @POST("care-takers/history/create")
    suspend fun addNewCategoryDetails(@Query("token") token: String,
                                      @Query("history_category_id") categoryId: Int,
                                      @Query("user_id") userId: Int,
                                      @Query("title") title: String,
                                      @Query("description") description: String): Response<MiniResponse>

    @GET("doctors/bookings")
    suspend fun getBookingsData(@Query("token") token: String): Response<BookingsData>

    @POST("doctors/bookings/cancel")
    suspend fun cancelBooking(@Query("token") token: String,
                              @Query("booking_id") bookingId: Int): Response<MiniResponse>

    @POST("doctors/check-code")
    suspend fun checkCode(@Query("token") token: String,
                          @Query("code") code: String,
                          @Query("user_id") userId: Int): Response<MiniResponse>
    @GET("guest/users")
    suspend fun getAllUsers(): Response<ChatUsers>

    @GET("guest/massages")
    suspend fun getChats(@Query("user_id") userId: Int): Response<ChatUsers>

    @POST("guest/massages/send")
    suspend fun sendMessage(@Query("from_id") currentUserId: Int,
                            @Query("to_id") receiverUserId: Int,
                            @Query("message") message: String): Response<MiniResponse>

    @GET("doctors/medications")
    suspend fun getMedicinesOfBooking(@Query("token") token: String,
                                      @Query("user_id") userId: Int): Response<MedicinesModel>

    @GET("care-takers/medications")
    suspend fun getMedicinesForUser(@Query("token") token: String,
                                    @Query("user_id") userId: Int): Response<MedicinesModel>

    @POST("doctors/medications/create")
    suspend fun addNewMedicine(@Query("token") token: String,
                               @Query("user_id") userId: Int,
                               @Query("medication") medicineName:String,
                               @Query("medication_dose") medicineDose: Int,
                               @Query("description") medicineDescription: String): Response<MiniResponse>

    @POST("care-takers/medications/create")
    suspend fun addNewMedicineForUser(@Query("token") token: String,
                               @Query("user_id") userId: Int,
                               @Query("medication") medicineName:String,
                               @Query("medication_dose") medicineDose: Int,
                               @Query("description") medicineDescription: String): Response<MiniResponse>

    @POST("doctors/medications/delete")
    suspend fun deleteMedicine(@Query("token") token: String,
                               @Query("medication_id") medicineId: Int): Response<MiniResponse>

    @POST("care-takers/medications/delete")
    suspend fun deleteMedicineForUser(@Query("token") token: String,
                               @Query("medication_id") medicineId: Int): Response<MiniResponse>

    @POST("doctors/medications/update")
    suspend fun updateMedicine(@Query("token") token: String,
                               @Query("medication_id") medicineId: Int,
                               @Query("medication") medicineName: String,
                               @Query("medication_dose") medicineDose: Int,
                               @Query("description") medicineDescription: String): Response<MiniResponse>

    @POST("care-takers/medications/update")
    suspend fun updateMedicineForUser(@Query("token") token: String,
                               @Query("medication_id") medicineId: Int,
                               @Query("medication") medicineName: String,
                               @Query("medication_dose") medicineDose: Int,
                               @Query("description") medicineDescription: String): Response<MiniResponse>
}