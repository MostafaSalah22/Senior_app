package com.project.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.project.domain.repo.Resource
import com.project.domain.model.AppUser
import com.project.domain.model.MiniResponse
import com.project.domain.model.ErrorResponse
import retrofit2.Response

    fun returnTrueResponse(response: Response<AppUser>): AppUser {
        if (response.code() == 403 || response.code() == 400) {
            val gson = Gson()
            val errorBody = response.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            val errorMessage = errorResponse.message

            return AppUser(successful = false, message = errorMessage, status = errorResponse.status)
        }
        return response.body()!!
    }

    fun returnChangeTrueResponse(response: Response<MiniResponse>): MiniResponse {
        if (response.code() == 403 || response.code() == 400) {
            val gson = Gson()
            val errorBody = response.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            val errorMessage = errorResponse.message

            return MiniResponse(successful = false, message = errorMessage, status = errorResponse.status)
        }
        return response.body()!!
    }

    fun <T:Any> handleResponse(response: Response<T>) : LiveData<Resource<T>?> {
        val resource: MutableLiveData<Resource<T>?> = MutableLiveData()
        try {

            if (response.isSuccessful) {
                response.body()?.let {
                    resource.value = Resource.Success(it)
                    return resource
                }
            }
            resource.value = Resource.Error(response.message())
            return resource
        } catch (e:Exception) {
            resource.value = Resource.Error(response.message())
            return resource
         }
    }