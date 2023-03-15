package ru.povtchat.security.hash

data class SaltedHash(
    val hash: String,
    val salt: String
)
