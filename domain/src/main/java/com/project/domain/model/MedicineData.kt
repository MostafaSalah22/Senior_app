package com.project.domain.model

data class MedicineData(
    val created_at: String,
    val description: String,
    val id: Int,
    val medication: String,
    val medication_dose: Int,
    val senior_id: String
)