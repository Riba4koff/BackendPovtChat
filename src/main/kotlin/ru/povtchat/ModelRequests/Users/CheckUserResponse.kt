package ru.povtchat.ModelRequests.Users

@kotlinx.serialization.Serializable
data class CheckUserResponse(
    val exist: Boolean
)
