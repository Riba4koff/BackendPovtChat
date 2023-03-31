package ru.povtchat.plugins

import com.backend.database.messages.Messages
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.povtchat.feautures.chats.chatSocket
import ru.povtchat.room.RoomController
import ru.povtchat.routes.messages
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val roomController = RoomController()
        chatSocket(roomController)
        messages(roomController)
    }
}