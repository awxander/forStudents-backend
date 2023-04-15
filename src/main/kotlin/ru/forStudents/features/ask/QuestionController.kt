package ru.forStudents.features.ask

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.forStudents.database.discussions.DiscussionDTO
import ru.forStudents.database.discussions.Discussions
import ru.forStudents.database.questions.QuestionDTO
import ru.forStudents.database.questions.Questions
import ru.forStudents.database.tokens.Tokens
import java.util.*

class QuestionController(private val call: ApplicationCall) {//TODO реализовать, доделать прием вопросом, протестить

    suspend fun openDiscussion() {
        val userToken = call.request.headers["Authorization"]
        if (userToken == null) {
            call.respond(HttpStatusCode.Forbidden, "token wasn't received  ")
        } else {
            val tokenDTO = Tokens.fetchToken(userToken)
            if (tokenDTO == null) {
                call.respond(HttpStatusCode.Unauthorized, "token expired or never exist, login again")
            } else {
                val newDiscussionId =  UUID.randomUUID().toString()
                Discussions.insert(DiscussionDTO(id = newDiscussionId))

                val questionModel = call.receive<RemoteQuestionModel>()
                Questions.insert(QuestionDTO(
                    id = UUID.randomUUID().toString(),
                    question = questionModel.body,
                    topic = questionModel.topic,
                    userEmail = tokenDTO.email,//токены сопоставлены с email, по токену берем email пользователя
                    discussionId = newDiscussionId
                ))
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}