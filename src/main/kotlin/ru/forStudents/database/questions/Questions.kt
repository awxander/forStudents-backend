package ru.forStudents.database.questions

import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Questions : Table("questions") {
    private val id = Questions.varchar("id", 50)
    private val topic = Questions.varchar("topic", 250)
    private val userEmail = Questions.varchar("user_email", 50)
    private val question = Questions.text("question_body")
    private val discussionId = Questions.varchar("discussion_id", 50)

    private val logger = LogManager.getLogger(Questions::class.java)

    fun insert(questionDTO: QuestionDTO) {
        transaction {
            Questions.insert {
                it[id] = questionDTO.id
                it[topic] = questionDTO.topic
                it[userEmail] = questionDTO.userEmail
                it[question] = questionDTO.question
                it[discussionId] = questionDTO.discussionId
            }
        }
    }

    fun fetchQuestions(email: String): List<QuestionDTO>? {
        return try {
            transaction {
                val questionsModel = Questions.select {
                    Questions.userEmail.eq(email)
                }.toList()
                val questions = ArrayList<QuestionDTO>()
                for (row in questionsModel) {
                    questions.add(
                        QuestionDTO(
                            id = row[Questions.id],
                            question = row[Questions.question],
                            topic = row[Questions.topic],
                            userEmail = row[Questions.userEmail],
                            discussionId = row[Questions.discussionId]
                        )
                    )
                }
                questions
            }
        } catch (ex: Exception) {
            logger.error("fetch questions failed, ${ex.message}")//TODO что с логгированием
            null
        }
    }

    fun fetchAll() : List<QuestionDTO>?{
        return try {
            transaction {
                val questionsModel = Questions.selectAll().toList()
                getQuestionsDTOList(questionsModel)
            }
        } catch (ex: Exception) {
            logger.error("fetch questions failed, ${ex.message}")//TODO что с логгированием
            null
        }
    }

    private fun getQuestionsDTOList(questionsModel: List<ResultRow>) : List<QuestionDTO>{
        val questions = ArrayList<QuestionDTO>()
        for (row in questionsModel) {
            questions.add(
                QuestionDTO(
                    id = row[Questions.id],
                    question = row[Questions.question],
                    topic = row[Questions.topic],
                    userEmail = row[Questions.userEmail],
                    discussionId = row[Questions.discussionId]
                )
            )
        }
        return questions
    }
}

