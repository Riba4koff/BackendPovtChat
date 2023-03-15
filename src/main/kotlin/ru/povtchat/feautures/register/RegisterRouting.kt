package com.backend.feautures.register

import com.backend.database.users.UserDTO
import com.backend.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.povtchat.security.hash.HashingService
import ru.povtchat.security.token.TokenConfig
import ru.povtchat.security.token.TokenService

fun Route.signUp(
    hashingService: HashingService
) {
    post("signup") {
        val request = call.receiveOrNull<RegisterRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, "check the fields for empty.")
            return@post
        }

        val areFieldsBlank =
            request.email.isBlank()
                    || request.login.isBlank()
                    || request.password.isBlank()
                    || request.username.isBlank()

        val isPwrdTooShort = request.password.length < 8

        if (areFieldsBlank || isPwrdTooShort) {
            call.respond(HttpStatusCode.Conflict, "Fields is empty or length password < 8")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(value = request.password)

        val user = UserDTO(
            login = request.login,
            password = saltedHash.hash,
            email = request.email,
            username = request.username,
            salt = saltedHash.salt
        )

        try {
            Users.insert(userDTO = user)
        } catch (e: ExposedSQLException) {
            call.respond("User already exists.")
        } catch (e: java.lang.IllegalArgumentException) {
            call.respond(e.message.toString())
        }

        call.respond(HttpStatusCode.OK, RegisterResponse(
            login = user.login,
            email = user.email,
            username = user.username,
            successful = true
        ))
    }
}