package com.backend.feautures.login

import com.backend.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.povtchat.ModelRequests.SignIn.AuthRequest
import ru.povtchat.ModelRequests.SignIn.AuthResponse
import ru.povtchat.database.tokens.Tokens
import ru.povtchat.security.hash.HashingService
import ru.povtchat.security.hash.SaltedHash
import ru.povtchat.security.token.TokenClaim
import ru.povtchat.security.token.TokenConfig
import ru.povtchat.security.token.TokenService
import java.util.*

fun Route.signIn(
    hashingService: HashingService,
    tokenService: TokenService,
    config: TokenConfig,
) {
    get("signin") {
        val request = call.receive<AuthRequest>()

        val user = Users.fetchUserByLogin(request.login)

        if (user == null) {
            call.respond(HttpStatusCode.Conflict, AuthResponse(invalidLoginOrPassword = true))
            return@get
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (isValidPassword.not()) {
            call.respond(HttpStatusCode.Conflict, AuthResponse(invalidLoginOrPassword = true))
        }

        val token = tokenService.generate(
            config,
            TokenClaim(
                name = "userId",
                value = user.login
            )
        )
        try {
            if (Tokens.getToken(user.login) != null) {
                Tokens.updateToken(_login = user.login, token = token)
            }
            Tokens.insertToken(
                _id = UUID.randomUUID().toString(),
                _login = user.login,
                _token = token
            )
        } catch (e: ExposedSQLException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.Conflict, e.message.toString())
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message.toString())
        }
        call.respond(
            HttpStatusCode.OK,
            AuthResponse(
                token = token,
                email = user.email,
                login = user.login,
                username = user.username,
                invalidLoginOrPassword = false
            )
        )
    }
}