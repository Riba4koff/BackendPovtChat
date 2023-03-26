package com.backend.feautures.register

import com.backend.database.users.UserDTO
import com.backend.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.povtchat.ModelRequests.SignUp.RegisterRequest
import ru.povtchat.ModelRequests.SignUp.RegisterResponse
import ru.povtchat.security.hash.HashingService

fun Route.signUp(
    hashingService: HashingService,
) {
    post("signup") {
        val request = call.receive<RegisterRequest>()
        
        val areFieldsBlank =
            request.email.isBlank()
                    || request.login.isBlank()
                    || request.password.isBlank()
                    || request.username.isBlank()

        val isPwdTooShort = request.password.length < 8
        if (areFieldsBlank || isPwdTooShort) {
            call.respond(HttpStatusCode.Conflict, "Fields is empty or length password < 8")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(value = request.password)
        UserDTO(
            login = request.login,
            password = saltedHash.hash,
            email = request.email,
            username = request.username,
            salt = saltedHash.salt
        ).let { user ->
            Users.fetchUserByLogin(request.login)?.let { _ ->
                call.respond(
                    RegisterResponse(
                        successful = false,
                        userHasAlreadyExists = true,
                        message = "Логин занят"
                    )
                )
            } ?: kotlin.run {
                Users.fetchUserByEmail(request.email)?.let {_ ->
                    call.respond(
                        RegisterResponse(
                            successful = false,
                            userHasAlreadyExists = true,
                            message = "Почта занята"
                        )
                    )
                } ?: kotlin.run {
                    Users.fetchUserByUsername(request.username)?.let { _ ->
                        call.respond(
                            RegisterResponse(
                                successful = false,
                                userHasAlreadyExists = true,
                                message = "Имя пользователя занято"
                            )
                        )
                    } ?: kotlin.run {
                        Users.insert(user)
                    }
                }
            }
            call.respond(
                HttpStatusCode.OK, RegisterResponse(
                    successful = true,
                    userHasAlreadyExists = false,
                    message = "Успешная регистрация"
                )
            )
        }
    }
}