package com.project.domain.model

data class MedicinesModel(
    val `data`: ArrayList<MedicineData>,
    val message: String,
    val successful: Boolean
)