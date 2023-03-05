package com.project.domain.model

data class MiniResponse(
    val message: String,
    val status: String? = null,
    val successful: Boolean
)