package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class GetMySeniorsUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke() = mainRepoInterface.getMySeniorsFromRemote()
    suspend fun handleResponse() = mainRepoInterface.handleGetMySeniorsResponse()
}