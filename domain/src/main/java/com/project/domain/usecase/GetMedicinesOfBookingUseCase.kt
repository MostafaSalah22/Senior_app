package com.project.domain.usecase

import com.project.domain.model.MedicinesModel
import com.project.domain.repo.MainRepoInterface

class GetMedicinesOfBookingUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int) = mainRepoInterface.getMedicines(userId)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MedicinesModel>()
}