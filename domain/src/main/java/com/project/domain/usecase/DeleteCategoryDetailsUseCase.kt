package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class DeleteCategoryDetailsUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(categoryDetailsId: Int) = mainRepoInterface.deleteCategoryDetails(categoryDetailsId)
    suspend fun handleResponse() = mainRepoInterface.handleDeleteCategoryDetailsResponse()
}