package ru.povtchat.plugins

import com.backend.feautures.login.signIn
import com.backend.feautures.register.signUp
import com.backend.security.authenticate
import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.povtchat.routes.editUserInfo
import ru.povtchat.security.hash.HashingService
import ru.povtchat.security.token.TokenConfig
import ru.povtchat.security.token.TokenService

fun Application.configureRouting(
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    routing {
        signIn(
            hashingService,
            tokenService,
            tokenConfig
        )
        signUp(
            hashingService
        )
        authenticate()
        editUserInfo()
    }
}