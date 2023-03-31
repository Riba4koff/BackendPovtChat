package ru.povtchat.ModelRequests.Users

@kotlinx.serialization.Serializable
data class DeleteUserByLoginResponse(
    val successful: Boolean,
    val message: String
)
