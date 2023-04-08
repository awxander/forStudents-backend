package ru.forStudents.database.discussions

import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Discussions : Table("discussions") {
    private val id = Discussions.varchar("id", 50)
    private val topic = Discussions.varchar("topic", 250)
    private val userEmail = Discussions.varchar("email", 50)
    private val question = Discussions.text("question")

    private val logger = LogManager.getLogger(Discussions::class.java)

    fun insert(discussionDTO: DiscussionDTO) {
        transaction {
            Discussions.insert {
                it[id] = discussionDTO.id
                it[topic] = discussionDTO.topic
                it[userEmail] = discussionDTO.userEmail
                it[question] = discussionDTO.question
            }
        }
    }

    fun fetchDiscussions(email: String): List<DiscussionDTO>? {
        return try {
            transaction {
                val discussionsModel = Discussions.select {
                    Discussions.userEmail.eq(email)
                }.toList()
            }
            return null
        } catch (ex: Exception) {
            println("yoyo\n")
            logger.info("user with email $email doesn't exist in db, ${ex.message} ")
            println("yoyo\n")
            null
        }
    }
}

