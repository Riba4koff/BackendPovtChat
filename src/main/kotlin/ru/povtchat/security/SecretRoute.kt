package com.backend.security

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.povtchat.security.AuthenticateResponse

fun Route.authenticate () {
    authenticate("jwt-auth") {
        post("/authenticate") {
            call.respond(HttpStatusCode.OK, AuthenticateResponse(message = "Authenticate was successfully.", successful = true))
        }
    }
}