package ru.forStudents.database.users

import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    private val password = Users.varchar("password", 20)
    private val username = Users.varchar("username", 30)
    private val email = Users.varchar("email", 50)

    private val logger = LogManager.getLogger(Users::class.java)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[password] = userDTO.password
                it[email] = userDTO.email
                it[username] = userDTO.username
            }
        }
    }

    fun fetchUser(email: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.select {
                    Users.email.eq(email)
                }.single()
                UserDTO(
                    password = userModel[Users.password],
                    email = userModel[Users.email],
                    username = userModel[Users.username]
                )
            }
        } catch (ex: Exception) {
            logger.error("user with email $email doesn't exist in db, ${ex.message} ")//TODO что с логгированием
            null
        }
    }
}

