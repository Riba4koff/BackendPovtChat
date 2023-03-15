package ru.povtchat.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.sessions.*
import io.ktor.util.*
import ru.povtchat.room.ChatSession
import ru.povtchat.security.token.TokenConfig

fun Application.configureSecurity(config: TokenConfig) {
    install(Sessions){
        cookie<ChatSession>("SESSION")
    }
    intercept(Plugins){
        val username = call.parameters["username"] ?: "Guest"
        if (call.sessions.get<ChatSession>() == null) call.sessions.set(ChatSession(username, generateNonce()))
    }
    install(Authentication) {
        jwt("jwt-auth") {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)){
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}
