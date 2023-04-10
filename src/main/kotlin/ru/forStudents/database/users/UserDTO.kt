package ru.forStudents.database.users

data class UserDTO(
    val password: String,
    val username: String,
    val email: String
)