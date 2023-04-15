package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class GetSeniorProfileUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int) = mainRepoInterface.getSeniorProfile(userId)
    suspend fun handleResponse() = mainRepoInterface.handleGetSeniorProfileResponse()
}