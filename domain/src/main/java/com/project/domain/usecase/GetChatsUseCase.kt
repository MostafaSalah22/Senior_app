package com.project.domain.usecase

import com.project.domain.model.ChatUsers
import com.project.domain.repo.MainRepoInterface

class GetChatsUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int) = mainRepoInterface.getChats(userId)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<ChatUsers>()
}