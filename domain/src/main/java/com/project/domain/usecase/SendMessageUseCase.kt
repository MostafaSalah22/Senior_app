package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface
import retrofit2.http.Query

class SendMessageUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(currentUserId: Int, receiverUserId: Int, message: String) =
                                     mainRepoInterface.sendMessage(currentUserId, receiverUserId, message)
}