package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class GetCategoryDetailsUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(categoryId: Int) = mainRepoInterface.getCategoryDetails(categoryId)
    suspend fun handleResponse() = mainRepoInterface.handleGetCategoryDetailsResponse()
}