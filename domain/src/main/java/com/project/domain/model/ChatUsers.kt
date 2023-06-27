package com.project.domain.model

data class ChatUsers(
    val `data`: ArrayList<ChatModel>,
    val message: String,
    val successful: Boolean
)
