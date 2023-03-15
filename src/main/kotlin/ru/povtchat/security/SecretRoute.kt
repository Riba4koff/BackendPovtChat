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
        get("authenticate") {
            call.respond(HttpStatusCode.OK, AuthenticateResponse("Successful"))
        }
    }
}

fun Route.getSecretInfo(){
    authenticate("jwt-auth") {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your login is: $userId")
        }
    }
}