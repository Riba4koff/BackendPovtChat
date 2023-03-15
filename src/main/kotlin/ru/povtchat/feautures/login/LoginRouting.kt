package com.backend.feautures.login

import com.backend.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.povtchat.security.hash.HashingService
import ru.povtchat.security.hash.SaltedHash
import ru.povtchat.security.token.TokenClaim
import ru.povtchat.security.token.TokenConfig
import ru.povtchat.security.token.TokenService

fun Route.signIn(
    hashingService: HashingService,
    tokenService: TokenService,
    config: TokenConfig,
) {
    post("signin") {
        val request = call.receive<AuthRequest>()

        val user = Users.fetchUserByLogin(request.login)

        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "User was not found.")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (isValidPassword.not()) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password.")
        }

        val token = tokenService.generate(
            config,
            TokenClaim(
                name = "userId",
                value = user.login
            )
        )

        call.respond(
            HttpStatusCode.OK,
            AuthResponse(
                token = token,
                email = user.email,
                login = user.login,
                username = user.username
            )
        )
    }
}