package com.project.domain.usecase

import com.project.domain.model.ChatUsers
import com.project.domain.repo.MainRepoInterface

class GetAllUsersUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke() = mainRepoInterface.getAllUsers()
    suspend fun handleResponse() = mainRepoInterface.handleResponse<ChatUsers>()
}