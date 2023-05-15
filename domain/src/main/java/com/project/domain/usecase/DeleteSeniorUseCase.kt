package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class DeleteSeniorUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int) = mainRepoInterface.deleteSenior(userId)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}