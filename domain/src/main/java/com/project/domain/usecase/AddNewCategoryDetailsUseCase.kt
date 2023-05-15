package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class AddNewCategoryDetailsUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(categoryId: Int, userId:Int, title:String, description:String) =
                                    mainRepoInterface.addNewCategoryDetails(categoryId, userId, title, description)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}