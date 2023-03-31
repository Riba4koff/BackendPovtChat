package ru.povtchat.ModelRequests.Users

@kotlinx.serialization.Serializable
data class DeleteAllUsersResponse(
    val successful: Boolean,
    val message: String
)
