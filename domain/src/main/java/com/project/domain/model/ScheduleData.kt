package com.project.domain.model

data class ScheduleData(
    val created_at: String,
    val date: String,
    val description: Any,
    val id: Int,
    val time: String,
    val title: String,
    val type: String
)