package com.project.domain.usecase

import com.project.domain.model.AppUser
import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class CheckCodeUseCase(private val mainRepoInterface: MainRepoInterface) {

    suspend operator fun invoke(code: String, userId: Int) = mainRepoInterface.checkCode(code, userId)

    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}