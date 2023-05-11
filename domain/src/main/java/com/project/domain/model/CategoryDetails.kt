package com.project.domain.model

data class CategoryDetails(
    val `data`: ArrayList<CategoryDetailsData>,
    val message: String,
    val successful: Boolean
)