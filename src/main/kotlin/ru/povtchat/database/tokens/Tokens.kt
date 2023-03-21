package ru.povtchat.database.tokens

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens : Table() {
    private val id = varchar("id", 50)
    private val login = varchar("login", 25)
    private val token = varchar("token", 200)

    fun updateToken(_login: String, token: String) {
        transaction {
            Tokens.update({ login eq _login }) {
                it[Tokens.token] = token
            }
        }
    }

    fun insertToken(_id: String, _login: String, _token: String) {
        transaction {
            Tokens.insert {
                it[id] = _id
                it[token] = _token
                it[login] = _login
            }
        }
    }

    private fun rowToTokenDTO(row: ResultRow) = TokenDTO(
        id = row[id],
        login = row[login],
        token = row[token]
    )

    fun getToken(_login: String) =
        try {
            transaction {
                val token = Tokens.select { login eq _login}.singleOrNull()
                if (token != null) {
                    rowToTokenDTO(token)
                }
                else null
            }
        } catch (e: Exception) {
            null
        }


    fun removeTokeN(_login: String) {
        transaction {
            Tokens.deleteWhere { login eq _login }
        }
    }
}