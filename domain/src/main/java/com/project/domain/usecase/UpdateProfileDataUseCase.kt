package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class UpdateProfileDataUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(name: String, username: String, phone: String, email: String) =
                                                mainRepoInterface.updateProfileData(name, username, phone, email)
    suspend fun handleResponse() = mainRepoInterface.handleUpdateProfileDataResponse()
}