package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class GetInformationCategoriesUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId:Int) = mainRepoInterface.getInformationCategories(userId)
    suspend fun handleResponse() = mainRepoInterface.handleGetInformationCategoriesResponse()
}