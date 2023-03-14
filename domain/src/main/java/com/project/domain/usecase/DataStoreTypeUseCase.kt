package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class DataStoreTypeUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend fun setType(type: String) = mainRepoInterface.setUserType(type)
    suspend fun getType() = mainRepoInterface.getUserType()
}