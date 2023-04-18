package ru.forStudents.features.question

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.forStudents.database.discussions.DiscussionDTO
import ru.forStudents.database.discussions.Discussions
import ru.forStudents.database.questions.QuestionDTO
import ru.forStudents.database.questions.Questions
import ru.forStudents.database.tokens.Tokens
import ru.forStudents.database.users.Users
import java.util.*
import kotlin.collections.ArrayList

class QuestionController(private val call: ApplicationCall) {

    suspend fun openDiscussion() {
        val userToken = call.request.headers["Authorization"]
        if (userToken == null) {
            call.respond(HttpStatusCode.Forbidden, "token wasn't received  ")
        } else {
            val tokenDTO = Tokens.fetchToken(userToken)
            if (tokenDTO == null) {
                call.respond(HttpStatusCode.Unauthorized, "token expired or never existed, login again")
            } else {
                val newDiscussionId = UUID.randomUUID().toString()
                Discussions.insert(DiscussionDTO(id = newDiscussionId))

                val questionModel = call.receive<ReceiveQuestionModel>()
                Questions.insert(
                    QuestionDTO(
                        id = UUID.randomUUID().toString(),
                        question = questionModel.body,
                        topic = questionModel.topic,
                        userEmail = tokenDTO.email,//токены сопоставлены с email, по токену берем email пользователя
                        discussionId = newDiscussionId
                    )
                )
                //TODO добавить push уведомления пользователям о новом вопросе
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun sendAllQuestions() {
        val userToken = call.request.headers["Authorization"]
        if (userToken == null) {
            call.respond(HttpStatusCode.Forbidden, "token wasn't received  ")
        } else {
            val tokenDTO = Tokens.fetchToken(userToken)
            if (tokenDTO == null) {
                call.respond(HttpStatusCode.Unauthorized, "token expired or never existed, login again")
            } else {
                val questions = Questions.fetchAll()
                val questionsResponseModel = ArrayList<ResponseQuestionModel>()
                if (questions != null) {
                    for (question in questions) {
                        val userDTO = Users.fetchUser(tokenDTO.email)
                        questionsResponseModel.add(ResponseQuestionModel(
                            email = userDTO!!.email,
                            userName = userDTO.username,
                            topic = question.topic,
                            body = question.question
                        ))
                    }

                    call.respond(questionsResponseModel)
                }else{
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

}