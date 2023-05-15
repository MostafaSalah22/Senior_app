package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class EditCategoryDetailsUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(categoryDetailsId: Int, title: String, description:String) =
                                                mainRepoInterface.editCategoryDetails(categoryDetailsId, title, description)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}