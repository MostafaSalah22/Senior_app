package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class ChangeProfilePasswordUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(oldPassword:String, newPassword:String, confirmPassword:String) =
                                                mainRepoInterface.changeProfilePassword(oldPassword, newPassword, confirmPassword)
    suspend fun handleResponse() = mainRepoInterface.handleChangePasswordResponse()
}