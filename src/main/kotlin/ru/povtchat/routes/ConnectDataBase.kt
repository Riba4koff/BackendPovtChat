package com.backend.database

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.connectDataBaseRouting(){
    routing {
        Database.connect(
            url = "jdbc:postgresql://localhost/postgres",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "хуй вам"
        )
    }
}
