package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class DeleteMedicineUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(medicineId: Int) = mainRepoInterface.deleteMedicine(medicineId)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}