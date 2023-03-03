package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class GetProfileDataAndUpdateDataStoreUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke() = mainRepoInterface.getProfileDataFromRemoteAndUpdateDataStore()
}