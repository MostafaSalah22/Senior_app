package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class AddNewSeniorUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(username: String) = mainRepoInterface.addNewSenior(username)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}