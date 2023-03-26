package ru.povtchat

import com.backend.database.connectDataBaseRouting
import ru.povtchat.plugins.configureRouting
import io.ktor.server.application.*
import ru.povtchat.plugins.*
import ru.povtchat.security.hash.SHA256HashingService
import ru.povtchat.security.token.JwtTokenService
import ru.povtchat.security.token.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    val config = TokenConfig(
        audience = environment.config.property("jwt.audience").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        expiresIn = 3L * 1000L * 60L * 60L * 24L,
        secret = environment.config.property("jwt.secret").getString()
    )
    val hashingService = SHA256HashingService()
    val tokenService = JwtTokenService()

    configureSecurity(config)
    configureRouting(
        hashingService,
        tokenService,
        config
    )
    configureSockets()
    connectDataBaseRouting()
    configureSerialization()

    configureMonitoring()
    connectDataBaseRouting()
}
