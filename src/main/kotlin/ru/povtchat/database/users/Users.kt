@file:Suppress("NAME_SHADOWING")

package com.backend.database.users

import com.backend.database.messages.Messages
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.povtchat.database.Util.Result

object Users : Table(name = "users") {
    private val login = Users.varchar("login", 25)
    private val password = Users.varchar("password", 100)
    private val username = Users.varchar("username", 30)
    private val email = Users.varchar("email", 25)
    private val salt = Users.varchar("salt", 100)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[username] = userDTO.username
                it[email] = userDTO.email ?: ""
                it[salt] = userDTO.salt
            }
        }
    }

    fun removeUser(login: String) {
        transaction {
            Users.deleteWhere { Users.login.eq(login) }
        }
    }

    fun editUser(
        oldLogin: String,
        newLogin: String,
        newEmail: String,
        newUsername: String,
        oldUsername: String
    ): Result<Unit> {
        return transaction {
            val userByLogin = fetchUserByLogin(newLogin)
            val userByUsername = fetchUserByUsername(newUsername)
            val userByEmail = fetchUserByEmail(newEmail)

            if (userByLogin == null || oldLogin == newLogin) {
                if (userByUsername == null || newUsername == oldUsername) {
                    if (userByEmail == null || userByEmail.login == oldLogin) {
                        transaction {
                            Users.update(where = { login eq oldLogin }) {
                                it[login] = newLogin
                                it[username] = newUsername
                                it[email] = newEmail
                                Messages.editUsername(oldUsername, newUsername)
                            }
                        }
                        Result.Success(message = "Успешное изменение")
                    } else Result.Error(message = "Почта занята.")
                } else Result.Error(message = "Имя пользователя занято.")
            } else Result.Error(message = "Логин занят.")
        }
    }

    private fun resultRowToUserDto(row: ResultRow) = UserDTO(
        login = row[login],
        password = row[password],
        username = row[username],
        email = row[email],
        salt = row[salt]
    )
    fun fetchUserByLogin(login: String): UserDTO? {
        return try {
            transaction {
                Users.select { Users.login.eq(login) }.singleOrNull()?.let { user ->
                    resultRowToUserDto(user)
                } ?: kotlin.run { null }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    suspend fun deleteAllUsers(){
        newSuspendedTransaction(Dispatchers.IO) {
            Users.deleteWhere { login neq "admin" }
        }
    }
    fun fetchUserByUsername(username: String): UserDTO? {
        return try {
            transaction {
                Users.select { Users.username.eq(username) }.singleOrNull()?.let { user ->
                    resultRowToUserDto(user)
                } ?: kotlin.run { null }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun fetchUserByEmail(email: String): UserDTO? {
        return try {
            transaction {
                Users.select { Users.email.eq(email) }.singleOrNull()?.let { user ->
                    resultRowToUserDto(user)
                } ?: kotlin.run { null }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
