package com.project.domain.model

data class ProfileUser(
    val `data`: ProfileData,
    val message: String? = null,
    val successful: Boolean
)