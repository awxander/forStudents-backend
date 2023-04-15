package ru.forStudents.database.tokens

import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.forStudents.database.users.UserDTO
import ru.forStudents.database.users.Users

object Tokens : Table("tokens") {

    private val id = Tokens.varchar("id",50)
    private val email = Tokens.varchar("email",30)
    private val token = Tokens.varchar("token",50)

    private val logger = LogManager.getLogger(Tokens::class.java)

    fun insert(tokenDTO: TokenDTO){
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.id
                it[email] = tokenDTO.email
                it[token] = tokenDTO.token
            }
        }
    }

    fun fetchToken(token: String): TokenDTO? {
        return try {
            transaction {
                val tokenModel = Tokens.select {
                    Tokens.token.eq(token)
                }.single()
                TokenDTO(
                    email = tokenModel[Tokens.email],
                    token = token,
                    id = tokenModel[Tokens.id]
                )
            }
        } catch (ex: Exception) {
            logger.error("token expired, ${ex.message} ")//TODO что с логгированием
            null
        }
    }


}