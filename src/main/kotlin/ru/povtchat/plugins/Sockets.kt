package ru.povtchat.plugins

import com.backend.feautures.chats.Connection
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import ru.povtchat.feautures.chats.chatSocket
import ru.povtchat.feautures.chats.getAllMessages
import ru.povtchat.room.RoomController
import java.time.Duration
import java.util.*
import kotlin.collections.LinkedHashSet

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
        getAllMessages(roomController)
    }
}