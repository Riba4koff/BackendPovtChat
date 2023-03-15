package com.backend.database.messages

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Messages : Table() {
    private val id_message = integer("id_message").autoIncrement()
    private val id_user = varchar("id_user", 50)
    private val id_chat = varchar("id_chat", 50)
    private val text = varchar("text", length = 500)
    private val time_sending = long("time_sending")


    fun insertMessage(messageDTO: MessageDTO) {
        transaction {
            Messages.insert {
                it[id_user] = messageDTO.id_user
                it[id_chat] = messageDTO.id_chat
                it[text] = messageDTO.text
                it[time_sending] = messageDTO.time_sending
            }
        }
    }

    fun deleteMessage(id_message: Int) {
        transaction {
            Messages.deleteWhere { Messages.id_message.eq(id_message) }
        }
    }

    private fun rowResultToMessageDTO(resultRow: ResultRow): MessageDTO =
        MessageDTO(
            id_user = resultRow[id_user],
            id_chat = resultRow[id_chat],
            text = resultRow[text],
            time_sending = resultRow[time_sending]
        )

    fun getAllMessages(): List<MessageDTO> = transaction {
        Messages.selectAll().map(::rowResultToMessageDTO).sortedByDescending { time_sending }
    }
}