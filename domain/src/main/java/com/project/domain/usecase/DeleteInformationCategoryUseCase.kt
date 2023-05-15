package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class DeleteInformationCategoryUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(categoryId: Int) = mainRepoInterface.deleteInformationCategory(categoryId)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}