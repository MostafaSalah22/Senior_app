package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class PostLoginUserUseCase(private val mainRepoInterface: MainRepoInterface) {

    suspend operator fun invoke(username:String, password:String) = mainRepoInterface.postLoginUser(username,password)
    suspend fun handleResponse() = mainRepoInterface.handleLoginResponse()
    suspend fun isEmailLoggedIn() = mainRepoInterface.isEmailLoggedIn()
}