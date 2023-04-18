package ru.forStudents

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import ru.forStudents.features.question.configureAskRouting
import ru.forStudents.features.login.configureLoginRouting
import ru.forStudents.features.register.configureRegisterRouting
import ru.forStudents.plugins.*

fun main() {

    println(Thread.currentThread().contextClassLoader.getResource("log4j2.properties"))

    Database.connect(
        url = "jdbc:postgresql://localhost:5432/forStudents",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "ububuz48"
    )


//    embeddedServer(CIO, port = 8080, host = "192.168.8.100", module = Application::module)
    embeddedServer(CIO, port = 8080, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureRouting()
    configureRegisterRouting()
    configureLoginRouting()
    configureSerialization()
    configureAskRouting()
}
