package ru.forStudents.features.question

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
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
                val questionsResponseModel = ArrayList<ResponseQuestionModel>()
                transaction {
                    val usersJoinQuestions = (Users innerJoin Questions)
                    val result = usersJoinQuestions.select {
                        Users.email.eq(Questions.userEmail)
                    }
                        .orderBy(Users.username)
                        .toList()
                    for (row in result) {
                        questionsResponseModel.add(
                            ResponseQuestionModel(
                                email = row[Users.email],
                                username = row[Users.username],
                                topic = row[Questions.topic],
                                body = row[Questions.question]
                            )
                        )
                    }
                }
                if (questionsResponseModel.size > 0)
                    call.respond(questionsResponseModel)
                else
                    call.respond(HttpStatusCode.OK)

            }
        }
    }

}