package com.project.domain.model

data class MySeniorsResponse(
    val `data`: ArrayList<MySeniorsData>,
    val message: String,
    val successful: Boolean
)