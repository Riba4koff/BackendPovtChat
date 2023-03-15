package com.backend.database.chats

import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO(
    val id_chat: String,
    val name: String,
    val create: Long,
    val id_last_message: String,
    val is_conversation: Boolean
)