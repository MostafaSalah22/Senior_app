package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class AddNewCategoryUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int, title:String) = mainRepoInterface.addNewCategory(userId, title)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}