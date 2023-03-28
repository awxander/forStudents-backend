package ru.forStudents.features

import ru.forStudents.database.users.Users

fun isUserExist(login: String)  = Users.fetchUser(login) != null

fun isPasswordCorrect(login : String, password: String) = Users.fetchUser(login)?.password == password