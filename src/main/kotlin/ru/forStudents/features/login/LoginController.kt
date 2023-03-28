package ru.forStudents.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.forStudents.database.tokens.TokenDTO
import ru.forStudents.database.tokens.Tokens
import ru.forStudents.features.isPasswordCorrect
import ru.forStudents.features.isUserExist
import java.util.*

class LoginController(private val call: ApplicationCall) {

    suspend fun loginUser() {
        val loginReceiveRemote = call.receive<LoginReceiveRemote>()

        if (!isUserExist(loginReceiveRemote.login)) {
            call.respond(HttpStatusCode.BadRequest, "user doesn't exist")
        } else {

            if (!isPasswordCorrect(loginReceiveRemote.login, loginReceiveRemote.password)) {
                call.respond(HttpStatusCode.Forbidden, "wrong password")
            } else {

                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        id = UUID.randomUUID().toString(),
                        login = loginReceiveRemote.login,
                        token = token
                    )
                )
                call.respond(LoginResponseRemote(token))
            }
        }
    }
}