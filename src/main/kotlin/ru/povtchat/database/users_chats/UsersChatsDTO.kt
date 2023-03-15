package com.backend.database.users_chats

import kotlinx.serialization.Serializable

@Serializable
data class UsersChatsDTO(
    val id_user: String,
    val id_chat: String
)
