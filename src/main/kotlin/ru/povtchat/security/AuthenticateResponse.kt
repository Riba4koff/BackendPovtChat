package ru.povtchat.security

@kotlinx.serialization.Serializable
data class AuthenticateResponse(
    val message: String
)
