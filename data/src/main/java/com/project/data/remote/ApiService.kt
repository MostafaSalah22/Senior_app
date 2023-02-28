package com.project.data.remote

import com.project.domain.model.AppUser
import com.project.domain.model.ChangeResponse
import com.project.domain.model.ProfileUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("care-takers/login")
    suspend fun postLoginUser(@Query("username")username:String,
                              @Query("password")password:String): Response<AppUser>

    @POST("care-takers/register")
    suspend fun postRegisterUser(@Query("username")username: String,
                                 @Query("name")name: String,
                                 @Query("password")password: String,
                                 @Query("confirm_password")confirm_password: String,
                                 @Query("phone")phone: String,
                                 @Query("email")email: String): Response<AppUser>

    @GET("care-takers/profile")
    suspend fun getProfileData(@Query("token")token:String): Response<ProfileUser>

    @POST("care-takers/profile/changePassword")
    suspend fun changeProfilePassword(@Query("token")token:String,
                                      @Query("old_password")oldPassword:String,
                                      @Query("password")newPassword:String,
                                      @Query("confirm_password")confirmPassword:String):Response<ChangeResponse>

    @Multipart
    @POST("care-takers/profile/changeImage")
    suspend fun changeProfileImage(@Query("token") token: String,
                                   @Part image: MultipartBody.Part):Response<ChangeResponse>
}