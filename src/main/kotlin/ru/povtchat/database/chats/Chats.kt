package com.backend.database.chats

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object Chats : Table() {
    private val id_chat = Chats.varchar("id_chat", 50).autoIncrement()
    private val name = Chats.varchar("id_chat", 25)
    private val create = Chats.long("create")
    private val is_conversation = Chats.bool("is_conversation")
    private val id_last_message = Chats.varchar("id_chat", 50)

    fun insertChat(chatDTO: ChatDTO){
        transaction {
            Chats.insert {
                it[name] = chatDTO.name
                it[create] = chatDTO.create
                it[is_conversation] = chatDTO.is_conversation
                it[id_last_message] = chatDTO.id_last_message
            }
        }
    }

    fun removeChat(id_chat: String) {
        transaction {
            Chats.deleteWhere { Chats.id_chat.eq(id_chat) }
        }
    }

    fun editChatName(id_chat: String, new_name: String) {
        transaction {
            Chats.update({Chats.id_chat.eq(id_chat)}){
                it[Chats.name] = new_name
            }
        }
    }
}