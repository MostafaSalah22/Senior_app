package com.project.domain.model

data class ProfileData(
    val birthdate: String? = null,
    val email: String,
    val id: Int,
    val image: String,
    val name: String,
    val phone: String,
    val username: String
)