package com.project.domain.usecase

import com.project.domain.repo.MainRepoInterface

class CancelBookingUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(bookingId: Int) = mainRepoInterface.cancelBooking(bookingId)

    suspend fun handleResponse() = mainRepoInterface.handleCancelBookingResponse()
}