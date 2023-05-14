package com.project.domain.model

data class BookingsDetails(
    val created_at: String,
    val date: String,
    val id: Int,
    val senior: SeniorProfileData,
    val status: String,
    val time: Any
)