package ru.povtchat.ModelRequests.EditUserInfoRequest

@kotlinx.serialization.Serializable
data class EditUsernameInfoResponse(
    val successful: Boolean = false,
    val message: String = ""
)
