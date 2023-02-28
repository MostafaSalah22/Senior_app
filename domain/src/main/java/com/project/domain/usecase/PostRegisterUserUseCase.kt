package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class PostRegisterUserUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(username: String, name: String,
                                password: String, confirm_password: String,
                                phone: String, email: String) =
                                                mainRepoInterface.postRegisterUser(username, name, password, confirm_password, phone, email)


    suspend fun handleResponse() = mainRepoInterface.handleRegisterResponse()
}