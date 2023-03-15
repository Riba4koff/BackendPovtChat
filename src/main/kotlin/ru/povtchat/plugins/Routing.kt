package ru.povtchat.plugins

import com.backend.feautures.login.signIn
import com.backend.feautures.register.signUp
import com.backend.security.authenticate
import com.backend.security.getSecretInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.povtchat.feautures.chats.chatSocket
import ru.povtchat.feautures.chats.getAllMessages
import ru.povtchat.room.RoomController
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
        getSecretInfo()
        get("/test") {
            call.respond(HttpStatusCode.OK, TestResponse("hello"))
        }
    }
}

@kotlinx.serialization.Serializable
data class TestResponse(
    val message: String
)
