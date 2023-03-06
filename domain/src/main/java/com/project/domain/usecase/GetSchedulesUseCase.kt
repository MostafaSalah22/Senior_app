package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class GetSchedulesUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int) = mainRepoInterface.getSchedulesFromRemote(userId)
    suspend fun handleResponse() = mainRepoInterface.handleGetSchedulesResponse()
}