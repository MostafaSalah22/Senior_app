package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class SendNotificationUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int, title: String, content: String) = mainRepoInterface.sendNotification(userId, title, content)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}