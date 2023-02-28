package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class GetProfileDataUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke() = mainRepoInterface.getProfileDataFromRemote()
    suspend fun handleResponse() = mainRepoInterface.handleProfileResponse()
}