package ru.forStudents.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.forStudents.database.users.UserDTO
import ru.forStudents.database.users.Users

object Tokens : Table("tokens") {

    private val id = Tokens.varchar("id",50)
    private val login = Tokens.varchar("login",20)
    private val token = Tokens.varchar("token",50)

    fun insert(tokenDTO: TokenDTO){
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.id
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }


}