package com.project.domain.usecase

import com.project.domain.model.MedicinesModel
import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class GetMedicinesOfBookingUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int) = mainRepoInterface.getMedicinesOfBooking(userId)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MedicinesModel>()
}