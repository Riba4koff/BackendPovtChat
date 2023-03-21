package ru.povtchat.routes

import com.backend.database.users.Users
import com.backend.security.authenticate
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.povtchat.ModelRequests.EditUserInfoRequest.EditUserInfoRequest
import ru.povtchat.ModelRequests.EditUserInfoRequest.EditUsernameInfoResponse
import ru.povtchat.database.Util.Result

fun Route.editUserInfo() {
    authenticate("jwt-auth"){
        post("edit-user-info") {
            val request = call.receive<EditUserInfoRequest>()

            if (request.newLogin.isBlank() || request.email.isBlank() || request.oldUsername.isBlank()) {
                call.respond(
                    HttpStatusCode.Conflict,
                    EditUsernameInfoResponse(successful = false, message = "Заполните все поля")
                )
                return@post
            }

            if (Users.fetchUserByLogin(request.newLogin) != null && request.newLogin != request.oldLogin) {
                call.respond(EditUsernameInfoResponse(successful = false, message = "Логин занят"))
                return@post
            } else {
                try {
                    val result = Users.editUser(
                        oldLogin = request.oldLogin,
                        newLogin = request.newLogin,
                        newEmail = request.email,
                        newUsername = request.newUsername,
                        oldUsername = request.oldUsername
                    )
                    when (result) {
                        is Result.Success -> {
                            call.respond(
                                HttpStatusCode.OK,
                                EditUsernameInfoResponse(successful = true,
                                    message = result.message
                                        ?: "Неизвестная ошибка изменения пользователя."
                                )
                            )
                        }
                        is Result.Error -> {
                            call.respond(
                                HttpStatusCode.OK,
                                EditUsernameInfoResponse(successful = false,
                                    message = result.message
                                        ?: "Неизвестная ошибка изменения пользователя."
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Conflict, e.message.toString())
                }
            }
        }
    }
}