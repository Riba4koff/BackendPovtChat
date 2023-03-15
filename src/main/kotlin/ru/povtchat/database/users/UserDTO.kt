package com.backend.database.users

import kotlinx.serialization.Serializable

@Serializable
class UserDTO(
    val login: String,
    val password: String,
    val email: String,
    val username: String,
    val salt: String
)

data class UserModel(
    val login: String,
    val password: String,
    val email: String?,
    val username: String,
    val salt: String
)

fun UserDTO.toUserModel(): UserModel =
    UserModel(
        login = login,
        password = password,
        username = username,
        email = email,
        salt = salt
    )