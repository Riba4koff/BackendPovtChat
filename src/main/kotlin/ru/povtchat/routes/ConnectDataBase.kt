package com.backend.database

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.connectDataBaseRouting(){
    routing {
        Database.connect(
            url = "postgresql://postgres:FtHCZKVPTPkAXpZs5FNk@containers-us-west-42.railway.app:7382/railway",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "FtHCZKVPTPkAXpZs5FNk"/*"2Q2LYE!DDF3e"*/
        )
    }
}