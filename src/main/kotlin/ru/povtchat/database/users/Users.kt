package com.backend.database.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table(name = "users") {
    private val login = Users.varchar("login", 25)
    private val password = Users.varchar("password", 100)
    private val username = Users.varchar("username", 30)
    private val email = Users.varchar("email", 25)
    private val salt = Users.varchar("salt", 100)

    fun insert(userDTO: UserDTO){
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

    fun removeUser(login: String){
        transaction {
            Users.deleteWhere { Users.login.eq(login) }
        }
    }

    fun updateUser(userDTO: UserDTO){
        transaction {
            Users.updateUser(userDTO)
        }
    }

    private fun resultRowToUserDto(row: ResultRow) = UserDTO(
        login = row[login],
        password = row[password],
        username = row[username],
        email = row[email],
        salt = row[salt]
    )

    fun allUsers() : List<UserDTO> = transaction { Users.selectAll().map(::resultRowToUserDto) }

    fun fetchUserByLogin(login: String) : UserDTO? {
        return try {
            transaction {
                val user = Users.select { Users.login.eq(login) }.singleOrNull()
                if (user != null){
                    UserDTO(
                        login = user[Users.login],
                        password = user[password],
                        email = user[email],
                        username = user[username],
                        salt = user[salt]
                    )
                }
                else null
            }
        }
        catch (e: Exception) {
            null
        }
    }
}
