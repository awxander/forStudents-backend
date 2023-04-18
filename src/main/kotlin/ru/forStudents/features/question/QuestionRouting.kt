package ru.forStudents.features.question

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureAskRouting() {
    routing {
        post("questions/new") {
            val questionController = QuestionController(call)
            questionController.openDiscussion()
        }

        get("questions/all"){
            val questionController = QuestionController(call)
            questionController.sendAllQuestions()
        }
    }
}