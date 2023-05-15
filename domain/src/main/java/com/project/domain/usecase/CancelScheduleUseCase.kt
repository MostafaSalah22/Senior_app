package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class CancelScheduleUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(scheduleId: Int) = mainRepoInterface.cancelSchedule(scheduleId)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}