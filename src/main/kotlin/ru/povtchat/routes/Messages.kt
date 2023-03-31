package ru.povtchat.routes

import com.backend.database.messages.Messages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.deleteAll
import ru.povtchat.ModelRequests.Messages.DeleteAllMessagesResponse
import ru.povtchat.room.RoomController

fun Route.messages(
    roomController: RoomController
){
    route("Delete"){
        post("/messages") {
            try {
                Messages.deleteAllMessages()
                call.respond(HttpStatusCode.OK, DeleteAllMessagesResponse(successful = true, message = "Сообщения удалены"))
            } catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, DeleteAllMessagesResponse(successful = false, message = "Ошибка удаления"))
            }
        }
    }
    get("/messages") {
        try {
            call.respond(HttpStatusCode.OK, roomController.getAllMessages())
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, e.message.toString())
        }
    }
}