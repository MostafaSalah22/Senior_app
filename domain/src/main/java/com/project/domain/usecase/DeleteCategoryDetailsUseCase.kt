package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class DeleteCategoryDetailsUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(categoryDetailsId: Int) = mainRepoInterface.deleteCategoryDetails(categoryDetailsId)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}