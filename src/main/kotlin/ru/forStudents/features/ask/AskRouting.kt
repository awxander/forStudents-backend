package ru.forStudents.features.ask

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.forStudents.features.register.RegisterController

fun Application.configureAskRouting() {
    routing {
        post("/ask") {
            val questionController = QuestionController(call)

        }
    }
}