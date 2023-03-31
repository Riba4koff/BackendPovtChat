package ru.povtchat.routes

import com.backend.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.povtchat.ModelRequests.Users.CheckUserResponse
import ru.povtchat.ModelRequests.Users.DeleteAllUsersResponse
import ru.povtchat.ModelRequests.Users.DeleteUserByLoginResponse

fun Route.users() {
    route("Delete") {
        post("User") {
            try {
                call.parameters["login"]?.let { login ->
                    Users.fetchUserByLogin(login).let { user ->
                        if (user == null) {
                            call.respond(
                                HttpStatusCode.Conflict,
                                DeleteUserByLoginResponse(
                                    successful = false,
                                    message = "Пользователь не найден"
                                )
                            )
                        } else {
                            if (user.login == "admin") {
                                call.respond(
                                    HttpStatusCode.Conflict,
                                    DeleteUserByLoginResponse(
                                        successful = false,
                                        message = "Отказано в доступе"
                                    )
                                )
                            } else {
                                Users.removeUser(login)
                                call.respond(
                                    HttpStatusCode.Conflict,
                                    DeleteUserByLoginResponse(
                                        successful = true,
                                        message = "Пользователь удалён"
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict,
                    DeleteUserByLoginResponse(successful = false, message = "Ошибка удаления")
                )
            }
        }
        post("Users") {
            Users.deleteAllUsers()
            call.respond(
                DeleteAllUsersResponse(
                    successful = true,
                    message = "Пользователи удалены"
                )
            )
        }
    }
}