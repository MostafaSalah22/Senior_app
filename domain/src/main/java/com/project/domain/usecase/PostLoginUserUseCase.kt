package com.project.domain.usecase

import com.project.domain.model.AppUser
import com.project.domain.repo.MainRepoInterface

class PostLoginUserUseCase(private val mainRepoInterface: MainRepoInterface) {

    suspend operator fun invoke(username:String, password:String) = mainRepoInterface.postLoginUser(username,password)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<AppUser>()
    suspend fun saveDataToDataStore() = mainRepoInterface.saveDataToDataStore()
    suspend fun isEmailLoggedIn() = mainRepoInterface.isEmailLoggedIn()
}