package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class EditInformationCategoryTitleUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(categoryId: Int, title: String) = mainRepoInterface.editInformationCategoryTitle(categoryId, title)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}