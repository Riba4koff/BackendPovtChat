package ru.povtchat.ModelRequests.SignUp

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val email: String,
    val password: String,
    val username: String
)
