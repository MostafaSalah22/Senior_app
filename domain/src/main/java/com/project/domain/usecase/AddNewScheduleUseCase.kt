package com.project.domain.usecase

import com.project.domain.model.MiniResponse
import com.project.domain.repo.MainRepoInterface

class AddNewScheduleUseCase(private val mainRepoInterface: MainRepoInterface) {
    suspend operator fun invoke(userId: Int, title: String, date: String, time: String, description: String) =
                                                    mainRepoInterface.addNewSchedule(userId, title, date, time, description)
    suspend fun handleResponse() = mainRepoInterface.handleResponse<MiniResponse>()
}