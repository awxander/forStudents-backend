package ru.forStudents.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    private val login = Users.varchar("login", 20)
    private val password = Users.varchar("password", 20)
    private val username = Users.varchar("username", 30)
    private val email = Users.varchar("email", 50)


    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[email] = userDTO.email ?: ""
                it[username] = userDTO.username
            }
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.select {
                    Users.login.eq(login)
                }.single()
                UserDTO(
                    login = userModel[Users.login],
                    password = userModel[Users.password],
                    email = userModel[Users.email],
                    username = userModel[Users.username]
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}

