package ru.forStudents.features.question

import kotlinx.serialization.Serializable

@Serializable
data class ResponseQuestionModel(
    val email : String,
    val username: String,
    val topic : String,
    val body: String
)