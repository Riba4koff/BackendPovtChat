package ru.povtchat.database.tokens

@kotlinx.serialization.Serializable
data class TokenDTO(
    val id: String,
    val login: String,
    val token: String
)