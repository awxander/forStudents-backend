package ru.forStudents.database.discussions

import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.forStudents.database.questions.QuestionDTO
import ru.forStudents.database.questions.Questions

object Discussions : Table("discussions") {
    private val id = Questions.varchar("id", 50)

    private val logger = LogManager.getLogger(Questions::class.java)

    fun insert(discussionDTO: DiscussionDTO) {
        transaction {
            Discussions.insert {
                it[Discussions.id] = discussionDTO.id
            }
        }
    }


}