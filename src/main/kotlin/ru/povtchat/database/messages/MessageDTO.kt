package com.backend.database.messages

import kotlinx.serialization.Serializable

@Serializable
data class MessageDTO(
    val id_message: Long ?= 0,
    val id_chat: Long,
    val id_user: String,
    val text: String,
    val time_sending: Long
)
