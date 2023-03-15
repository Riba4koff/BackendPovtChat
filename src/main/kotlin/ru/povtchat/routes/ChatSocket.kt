package ru.povtchat.feautures.chats

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import ru.povtchat.room.ChatSession
import ru.povtchat.room.MemberAlreadyExistsException
import ru.povtchat.room.RoomController

fun Route.chatSocket(roomController: RoomController) {
    webSocket("/chat-socket") {
        val session = call.sessions.get<ChatSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }
        try {
            roomController.onJoin(
                session.username,
                session.sessionId,
                this
            )
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    roomController.sendMessage(
                        senderUsername = session.username,
                        message = frame.readText()
                    )
                }
            }
        } catch (e: MemberAlreadyExistsException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            roomController.tryDisconnect(username = session.username)
        }
    }
}

fun Route.getAllMessages(roomController: RoomController) {
    get("/messages") {
        call.respond(HttpStatusCode.OK, roomController.getAllMessages())
    }
}