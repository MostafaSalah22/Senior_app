package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class UpdateMedicineUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(medicineId: Int,
                                medicineName: String,
                                medicineDose: Int,
                                medicineDescription: String) = mainRepoInterface.updateMedicine(medicineId, medicineName, medicineDose, medicineDescription)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}