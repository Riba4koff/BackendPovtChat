package com.backend.feautures.login

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val login: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val login: String,
    val email: String,
    val username: String,
    val token: String,
)