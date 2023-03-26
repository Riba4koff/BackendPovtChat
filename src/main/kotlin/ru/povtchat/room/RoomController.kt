package ru.povtchat.room

import com.backend.database.messages.MessageDTO
import com.backend.database.messages.Messages
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

interface IRoomController {
    fun onJoin(username: String, sessionId: String, session: WebSocketSession)
    suspend fun sendMessage(senderUsername: String, message: String)
    suspend fun sendMessageToOneUser(
        senderUsername: String,
        targetUsername: String,
        message: String,
    )

    suspend fun tryDisconnect(username: String)
    suspend fun getAllMessages(): List<MessageDTO>
}


class RoomController : IRoomController {
    private val members = ConcurrentHashMap<String, Member>()
    override fun onJoin(
        username: String, sessionId: String,
        session: WebSocketSession
    ) {
        if (members.containsKey(username)) throw MemberAlreadyExistsException()
        members[username] = Member(
            username,
            sessionId,
            session
        )
    }
    override suspend fun sendMessage(
        senderUsername: String,
        message: String
    ) {
        MessageDTO(
            id_chat = 0,
            text = message,
            id_user = senderUsername,
            time_sending = System.currentTimeMillis()
        ).let { dto ->
            Messages.insertMessage(dto)
            Messages.fetchMessageByTimeSending(dto.time_sending).let { messageDTO ->
                members.values.forEach { member ->
                    Json.encodeToString(messageDTO).let { parsedMessage ->
                        member.socket.send(Frame.Text(parsedMessage))
                    }
                }
            }
        }
    }
    override suspend fun sendMessageToOneUser(
        senderUsername: String,
        targetUsername: String,
        message: String,
    ) {
        //TODO REALIZE SEND MESSAGE TO ONE USER
    }
    override suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        if (members.containsKey(username)) {
            members.remove(username)
        }
    }
    override suspend fun getAllMessages(): List<MessageDTO> {
        return Messages.getAllMessages()
    }
}
