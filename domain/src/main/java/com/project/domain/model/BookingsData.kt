package com.project.domain.model

data class BookingsData(
    val `data`: ArrayList<BookingsDetails>,
    val message: String,
    val successful: Boolean
)