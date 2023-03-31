val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
}

group = "ru.povtchat"
version = "0.0.1"

application {
    //mainClass.set("io.ktor.server.netty.EngineMain")
    mainClass.set("ru.povtchat.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


repositories {
    mavenCentral()
}

ktor {
    fatJar {
        archiveFileName.set("povtchat.jar")
    }
}

dependencies {

    //Call Logging
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")

    //Content Negotiation
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")

    //server core jvm
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")


    //kotlinx.serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    //authentication
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    //netty
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")

    //logback
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //postgres
    implementation("org.postgresql:postgresql:42.2.2")

    //exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    //apache.common.codec
    implementation("commons-codec:commons-codec:1.5")

    //WebSockets
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //test
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}