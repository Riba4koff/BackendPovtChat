package ru.povtchat.security.token

data class TokenConfig(
    val audience: String,
    val issuer: String,
    val expiresIn: Long,
    val secret: String
)
