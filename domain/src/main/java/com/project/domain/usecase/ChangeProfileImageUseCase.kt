package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface
import okhttp3.MultipartBody

class ChangeProfileImageUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(file: MultipartBody.Part) = mainRepoInterface.changeProfileImage(file)
    suspend fun getProfileDataFromRemoteAndUpdateDataStore() = mainRepoInterface.getProfileDataFromRemoteAndUpdateDataStore()
    suspend fun handleResponse() = mainRepoInterface.handleChangeImageResponse()
}