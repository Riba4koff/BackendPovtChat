package ru.povtchat.ModelRequests.Messages

@kotlinx.serialization.Serializable
data class DeleteAllMessagesResponse(
    val successful: Boolean,
    val message: String
)
