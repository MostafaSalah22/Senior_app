package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class AddNewMedicineUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int,
                                medicineName: String,
                                medicineDose: Int,
                                medicineDescription: String) = mainRepoInterface.addNewMedicine(userId, medicineName, medicineDose, medicineDescription)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}