package ru.povtchat.ModelRequests.SignIn

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val login: String? = "",
    val email: String? = "",
    val username: String? = "",
    val token: String ?= "",
    val invalidLoginOrPassword: Boolean = true
)
