package com.project.domain.model

data class ChangeResponse(
    val message: String,
    val status: String? = null,
    val successful: Boolean
)