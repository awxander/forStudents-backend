package ru.forStudents.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.forStudents.database.tokens.TokenDTO
import ru.forStudents.database.tokens.Tokens
import ru.forStudents.database.users.UserDTO
import ru.forStudents.database.users.Users
import ru.forStudents.features.isUserExist
import ru.forStudents.utils.isValidEmail
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {

        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()

        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "not valid email")
            return
        }
        if (isUserExist(registerReceiveRemote.email)) {
            call.respond(HttpStatusCode.BadRequest, "user already exist")
        } else {
            val token = UUID.randomUUID().toString()
            Tokens.insert(
                TokenDTO(
                    id = UUID.randomUUID().toString(),
                    email = registerReceiveRemote.email,
                    token = token
                )
            )
            Users.insert(UserDTO(
                password = registerReceiveRemote.password,
                email = registerReceiveRemote.email,
                username = registerReceiveRemote.username
            ))
            call.respond(RegisterResponseRemote(token = token))
        }
    }


}