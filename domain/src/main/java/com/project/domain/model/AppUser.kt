package com.project.domain.model

data class AppUser(
    val `data`: Data? = null,
    var message: String? = null,
    var successful: Boolean,
    var status: String? = null
)