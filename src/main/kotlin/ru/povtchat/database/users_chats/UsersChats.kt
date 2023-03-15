package com.backend.database.users_chats

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object UsersChats : Table(name = "users_chats") {
    private val id_user = UsersChats.varchar("id_user", 50)
    private val id_chat = UsersChats.varchar("id_chat", 50)

    fun insert(users_chats: UsersChatsDTO){
        transaction {
            UsersChats.insert {
                it[id_user] = users_chats.id_user
                it[id_chat] = users_chats.id_chat
            }
        }
    }
    fun remove(users_chats: UsersChatsDTO){
        transaction {
            UsersChats.deleteWhere {
                id_user.eq(users_chats.id_user)
                id_chat.eq(users_chats.id_chat)
            }
        }
    }
}