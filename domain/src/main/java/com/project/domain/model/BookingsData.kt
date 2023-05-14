package com.project.domain.model

data class BookingsData(
    val `data`: List<BookingsDetails>,
    val message: String,
    val successful: Boolean
)