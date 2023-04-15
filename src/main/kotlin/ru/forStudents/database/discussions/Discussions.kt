package ru.forStudents.database.discussions

import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object Discussions : Table("discussions") {
    private val id = Discussions.varchar("id", 50)

    private val logger = LogManager.getLogger(Discussions::class.java)

    fun insert(discussionDTO: DiscussionDTO) {
        transaction {
            Discussions.insert {
                it[Discussions.id] = discussionDTO.id
            }
        }
    }


}