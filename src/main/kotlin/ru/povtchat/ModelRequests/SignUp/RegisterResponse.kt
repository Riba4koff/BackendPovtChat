package ru.povtchat.ModelRequests.SignUp

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val successful: Boolean,
    val userHasAlreadyExists: Boolean,
    val message: String
)
