package ru.povtchat.ModelRequests.SignIn

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val login: String,
    val password: String
)