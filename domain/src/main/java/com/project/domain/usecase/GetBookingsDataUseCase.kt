package com.project.domain.usecase

import com.project.domain.model.BookingsData
import com.project.domain.repo.MainRepoInterface

class GetBookingsDataUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke() = mainRepoInterface.getBookingsData()
    suspend fun handleResponse() = mainRepoInterface.handleResponse<BookingsData>()
}