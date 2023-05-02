package com.project.data.remote

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

    @GET("guest/seniors/profile")
    suspend fun getSeniorProfile(@Query("user_id") userId: Int): Response<SeniorProfile>

    @GET("care-takers/history-categories")
    suspend fun getInformationCategories(@Query("token") token: String,
                                         @Query("user_id") userId: Int): Response<InformationCategories>
}